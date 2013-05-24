package ErraiLearning.client.shared;

import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class Invitation {
	
	private Player inviter = null;
	private Player invitee = null;
	
	public Invitation() {}
	
	public Invitation(Player inviter, Player invitee) {
		this.setInviter(inviter);
		this.setInvitee(invitee);
	}

	public Player getInviter() {
		return inviter;
	}

	public void setInviter(Player inviter) {
		this.inviter = inviter;
	}

	public Player getInvitee() {
		return invitee;
	}

	public void setInvitee(Player invitee) {
		this.invitee = invitee;
	}

}
