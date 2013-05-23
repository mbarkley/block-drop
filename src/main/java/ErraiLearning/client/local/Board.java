package ErraiLearning.client.local;

import org.jboss.errai.ui.nav.client.local.Page;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;

@Templated
@Page
public class Board extends Composite {
	
	public Board() {
		System.out.println("Board constructor called.");
	}

}
