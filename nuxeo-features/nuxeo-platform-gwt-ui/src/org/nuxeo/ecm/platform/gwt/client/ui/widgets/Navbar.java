/*
 * (C) Copyright 2006-2008 Nuxeo SA (http://nuxeo.com/) and others.
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
 *     bstefanescu
 *
 * $Id$
 */

package org.nuxeo.ecm.platform.gwt.client.ui.widgets;

import org.nuxeo.ecm.platform.gwt.client.Framework;
import org.nuxeo.ecm.platform.gwt.client.JSHandler;
import org.nuxeo.ecm.platform.gwt.client.model.Url;
import org.nuxeo.ecm.platform.gwt.client.ui.UI;
import org.nuxeo.ecm.platform.gwt.client.ui.login.LoginDialog;
import org.nuxeo.ecm.platform.gwt.client.ui.login.LogoutCommand;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.HistoryListener;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public class Navbar extends HLayout implements HistoryListener {

    protected boolean firstTime = true;
    protected String username = null;
    protected HTMLFlow span;

    /**
     *
     */
    public Navbar() {
        refresh();
        History.addHistoryListener(this);
        Framework.registerJSHandler("login", new JSHandler() {
          public Object onEvent(String data) {
              LoginDialog dlg = new LoginDialog();
              dlg.show();
              return null;
        }
        });
        Framework.registerJSHandler("logout", new JSHandler() {
            public Object onEvent(String data) {
                new LogoutCommand().execute();
                return null;
          }
          });
    }


    public void refresh() {
        ToolStrip navbar = new ToolStrip();
        navbar.setStyleName("navbar");
        navbar.setAlign(Alignment.RIGHT);

        span = new HTMLFlow(createContents());
        navbar.addMember(span);
        span.setWidth100();

        addMember(navbar);


//        ToolStripSeparator sep = new ToolStripSeparator();
//
//        String username = UI.getContext().getUsername();
//        if (username == null) username = "Guest";
//        Label label = new Label(username);
//        label.setAutoFit(false);
//        label.setAlign(Alignment.RIGHT);
//        navbar.addMember(label);
//
//        navbar.addMember(sep);
//
//        Button b = new Button(" Logout ");
//        b.setBaseStyle("docButton");
//        //b.setAutoFit(true);
//        navbar.addMember(b);
//        b.setOverflow(Overflow.VISIBLE);
//        b.setWidth(1);
//        //b.setShowRollOver(false);
//        //b.setAutoFit(true);
//        navbar.addMember(sep);


//        f = new HTMLFlow("<a href=\"#settings\">Settings</a>");
//        //b.setBaseStyle("docButton");
//        //b.setAutoFit(true);
//        navbar.addMember(f);
//        f.setWidth(1);
//
//        navbar.addMember(sep);
//
//        f = new HTMLFlow("<a href=\"#help\">Help</a>");
//        //b.setBaseStyle("docButton");
//        //b.setAutoFit(true);
//        navbar.addMember(f);
//        f.setWidth(1);
//        navbar.addMember(sep);
//
//        f = new HTMLFlow("<a href=\"#about\">About</a>");
//        //f.setBaseStyle("docButton");
//        //b.setAutoFit(true);
//        navbar.addMember(f);
//        f.setWidth(1);
        //addChild(navbar);
        //navbar.setSnapTo("TR");

    }



    protected String createContents() {
        username = Framework.getUserName();
        String loginCode = null;
        if (username == null) {
            username = Framework.getAnonymousUserName();
            loginCode = "<a href=\"#\" onCLick=\"nx.fire('login'); return false;\">Login</a>";
        } else if (username.equals(Framework.getAnonymousUserName())) {
            loginCode = "<a href=\"#\" onCLick=\"nx.fire('login'); return false;\">Login</a>";
        } else {
            loginCode = "<a href=\"#\" onCLick=\"nx.fire('logout'); return false;\">Logout</a>";
        }
        return "<span class=\"navbarSpan\"><b>"+username+"</b> | " + loginCode +
        " | <a href=\"#settings\">Settings</a>" +
        " | <a href=\"#help\">Help</a>" +
        " | <a href=\"#about\">About</a></span>";
    }


    public void onHistoryChanged(String historyToken) {
        if (historyToken.equals("help")) {
            Url url = new Url("/help");
            UI.openInEditor(url);
        } else if (historyToken.equals("about")) {
            Url url = new Url("/about");
            UI.openInEditor(url);
        } else if (historyToken.equals("settings")) {
            Url url = new Url("/settings");
            UI.openInEditor(url);
        }
    }

}
