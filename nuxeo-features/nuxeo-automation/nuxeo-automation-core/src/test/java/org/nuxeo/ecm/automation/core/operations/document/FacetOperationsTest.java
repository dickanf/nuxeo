/**
 *
 */

package org.nuxeo.ecm.automation.core.operations.document;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.automation.*;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.test.CoreFeature;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import com.google.inject.Inject;
import org.nuxeo.runtime.test.runner.LocalDeploy;

/**
 * @author Thibaud Arguillere
 */

@RunWith(FeaturesRunner.class)
@Features(CoreFeature.class)
@Deploy("org.nuxeo.ecm.automation.core")
@LocalDeploy("org.nuxeo.ecm.automation.core:OSGI-INF/add-facet-test-contrib.xml")
public class FacetOperationsTest {

    @Inject
    CoreSession session;

    @Inject
    AutomationService service;

    // MyNewFacet is declared in OSGI-INF/add-facet-test-contrib.xml
    public static final String THE_FACET = "MyNewFacet";

    protected DocumentModel folder;

    protected DocumentModel docNoFacet;

    protected DocumentModel docWithFacet;

    @Before
    public void initRepo() throws Exception {
        session.removeChildren(session.getRootDocument().getRef());
        session.save();

        folder = session.createDocumentModel("/", "Folder", "Folder");
        folder.setPropertyValue("dc:title", "Folder");
        folder = session.createDocument(folder);
        session.save();
        folder = session.getDocument(folder.getRef());

        docNoFacet = session.createDocumentModel("/Folder", "DocNoFacet", "File");
        docNoFacet.setPropertyValue("dc:title", "DocNotFacet");
        docNoFacet = session.createDocument(docNoFacet);
        session.save();
        docNoFacet = session.getDocument(docNoFacet.getRef());

        docWithFacet = session.createDocumentModel("/Folder", "DocWithFacet", "File");
        docWithFacet.setPropertyValue("dc:title", "DocWithFacet");
        docWithFacet = session.createDocument(docWithFacet);
        session.save();
        docWithFacet = session.getDocument(docWithFacet.getRef());

        docWithFacet.addFacet(THE_FACET);
        docWithFacet = session.saveDocument(docWithFacet);
        session.save();

    }
    
    @After
    public void cleanup() {
        session.removeChildren(session.getRootDocument().getRef());
        session.save();
    }

    @Test
    public void testAddFacet() throws OperationException {
        
        assertNotNull(docNoFacet);
        assertFalse("New doc should not have the facet.", docNoFacet.hasFacet(THE_FACET));

        OperationContext ctx = new OperationContext(session);
        ctx.setInput(docNoFacet);
        OperationChain chain = new OperationChain("testAddFacet");
        chain.add(AddFacet.ID).set("facet", THE_FACET);
        DocumentModel resultDoc = (DocumentModel)service.run(ctx, chain);

        assertNotNull(resultDoc);
        assertTrue("The doc should now have the facet.", resultDoc.hasFacet(THE_FACET));        

    }

    @Test
    public void testRemoveFacet() throws OperationException {

        //remove from a document with facet
        assertNotNull(docWithFacet);
        assertTrue("New doc should have the facet.", docWithFacet.hasFacet(THE_FACET));

        OperationContext ctx = new OperationContext(session);
        ctx.setInput(docNoFacet);
        OperationChain chain = new OperationChain("testRemoveFacet");
        chain.add(RemoveFacet.ID).set("facet", THE_FACET);
        DocumentModel resultDoc = (DocumentModel)service.run(ctx, chain);

        assertNotNull(resultDoc);
        assertFalse("The doc should not have the facet.", resultDoc.hasFacet(THE_FACET));

    }

}