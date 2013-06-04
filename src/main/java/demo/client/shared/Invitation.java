package demo.client.shared;

import org.jboss.errai.common.client.api.annotations.Portable;

import demo.client.shared.Player;

/*
 * A portable JavaBean for transmitting invitations to games between clients.
 */
@Portable
public class Invitation {
	
	/* The player who initiated the invitation. */
	private Player inviter = null;
	/* The player to whom the invitation is targetted. */
	private Player invitee = null;
	/* The status of whether or not the invitee has accepted this invitation. */
	private boolean accepted = false;
	
	/*
	 * A default no-arg constructor for JavaBean compliance. Users should prefer the 
	 * constructor with Player arguments.
	 */
	public Invitation() {}
	
	/*
	 * Create an invitation from inviter to invitee.
	 * 
	 * @param inviter The player who initiated the invitation.
	 * 
	 * @param invitee The player to whom the invitation is targetted.
	 */
	public Invitation(Player inviter, Player invitee) {
		this.setInviter(inviter);
		this.setInvitee(invitee);
	}

	/*
	 * Get the player who sent this invitation.
	 * 
	 * @return The player who sent this invitation.
	 */
	public Player getInviter() {
		return inviter;
	}

	/*
	 * Set the player who is sending this invitation.
	 * 
	 * @param The player who is sending this invitation.
	 */
	public void setInviter(Player inviter) {
		this.inviter = inviter;
	}

	/*
	 * Get the player who this invitation is targetted to.
	 * 
	 * @return The player to whom the invitation is targetted.
	 */
	public Player getInvitee() {
		return invitee;
	}

	/*
	 * Set the player who is receiving this invitation.
	 * 
	 * @param The player to whom this invitation is targetting.
	 */
	public void setInvitee(Player invitee) {
		this.invitee = invitee;
	}

	/*
	 * Check if this invitation has been accepted. This flag is defaulted to false
	 * if there has not yet been a response.
	 * 
	 * @return Return true iff the invitee has accepted this invitation.
	 */
	public boolean isAccepted() {
		return accepted;
	}

	/*
	 * Set whether or not this invitation has been accepted by the invitee.
	 * 
	 * @param accepted Should be true iff the invitee accepts this invitation.
	 */
	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}

}
