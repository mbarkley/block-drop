package demo.client.local;

import javax.annotation.PostConstruct;

import org.jboss.errai.ui.nav.client.local.DefaultPage;
import org.jboss.errai.ui.nav.client.local.Page;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.user.client.ui.Composite;

/*
 * An Errai Navigation Page providing the UI for a tic-tac-toe game.
 */
@Page(role=DefaultPage.class)
@Templated
public class Board extends Composite {
	
}
