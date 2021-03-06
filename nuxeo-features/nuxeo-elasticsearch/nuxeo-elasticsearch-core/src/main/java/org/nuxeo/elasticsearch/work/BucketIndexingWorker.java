/*
 * (C) Copyright 2014 Nuxeo SA (http://nuxeo.com/) and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     Nuxeo
 */

package org.nuxeo.elasticsearch.work;

import static org.nuxeo.elasticsearch.ElasticSearchConstants.REINDEX_BUCKET_WRITE_PROPERTY;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentLocation;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.query.sql.NXQL;
import org.nuxeo.ecm.core.work.api.Work;
import org.nuxeo.elasticsearch.api.ElasticSearchIndexing;
import org.nuxeo.elasticsearch.commands.IndexingCommand;
import org.nuxeo.elasticsearch.commands.IndexingCommand.Type;
import org.nuxeo.elasticsearch.Timestamp;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.transaction.TransactionHelper;

/**
 * œ Worker to index a bucket of documents
 *
 * @since 7.1
 */
public class BucketIndexingWorker extends BaseIndexingWorker implements Work {
    private static final Log log = LogFactory.getLog(BucketIndexingWorker.class);

    private static final long serialVersionUID = -4665673026513796882L;

    private static final String DEFAULT_BUCKET_SIZE = "50";

    private final boolean warnAtEnd;

    private final int documentCount;

    public BucketIndexingWorker(String repositoryName, List<String> docIds, boolean warnAtEnd) {
        setDocuments(repositoryName, docIds);
        documentCount = docIds.size();
        this.warnAtEnd = warnAtEnd;
    }

    @Override
    public String getTitle() {
        return " ElasticSearch bucket indexer size " + documentCount;
    }

    @Override
    protected void doWork() {
        ElasticSearchIndexing esi = Framework.getLocalService(ElasticSearchIndexing.class);
        openSystemSession();
        int bucketSize = Math.min(documentCount, getBucketSize());
        List<String> ids = new ArrayList<>(bucketSize);
        for (DocumentLocation doc : getDocuments()) {
            ids.add(doc.getIdRef().value);
            if ((ids.size() % bucketSize) == 0) {
                esi.indexNonRecursive(getIndexingCommands(session, ids));
                ids.clear();
                TransactionHelper.commitOrRollbackTransaction();
                TransactionHelper.startTransaction();
            }
        }
        if (!ids.isEmpty()) {
            esi.indexNonRecursive(getIndexingCommands(session, ids));
            ids.clear();
        }
        if (warnAtEnd) {
            log.warn(String.format("Re-indexing job: %s completed.", getSchedulePath().getParentPath()));
        }
    }

    private List<IndexingCommand> getIndexingCommands(CoreSession session, List<String> ids) {
        List<IndexingCommand> ret = new ArrayList<>(ids.size());
        long now = Timestamp.currentTimeMicros();
        for (DocumentModel doc : fetchDocuments(session, ids)) {
            IndexingCommand cmd = new IndexingCommand(doc, Type.INSERT, false, false);
            cmd.setOrder(now);
            ret.add(cmd);
        }
        return ret;
    }

    private List<DocumentModel> fetchDocuments(CoreSession session, List<String> ids) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM Document, Relation WHERE ecm:uuid IN (");
        for (int i = 0; i < ids.size(); i++) {
            sb.append(NXQL.escapeString(ids.get(i)));
            if (i < ids.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append(")");
        // read invalidation
        session.save();
        return session.query(sb.toString());
    }

    protected int getBucketSize() {
        String value = Framework.getProperty(REINDEX_BUCKET_WRITE_PROPERTY, DEFAULT_BUCKET_SIZE);
        return Integer.parseInt(value);
    }

}
