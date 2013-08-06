package demo.client.local.game.tools;

import com.eemi.gwt.tour.client.GwtTour;
import com.eemi.gwt.tour.client.Placement;
import com.eemi.gwt.tour.client.Tour;
import com.eemi.gwt.tour.client.TourStep;
import com.google.gwt.user.client.Timer;

public class CallOutManager {
  
  private static final int timeout = 5000;
  
  public static void createAttackCallout(final String elementId, String playerName) {
    GwtTour.endTour();
    
    TourStep step = new TourStep(Placement.LEFT, elementId);
    step.setContent(playerName + " attacked you!");
    step.setShowNextButton(false);
    step.setShowPrevButton(false);
    step.setZIndex(1);
    step.setWidth(100);
    
    final Tour tour = new Tour(elementId);
    tour.setNextOnTargetClick(true);
    tour.setDoneBtnText("Close");
    tour.setShowCloseButton(false);
    tour.addStep(step);
    
    Timer timer = new Timer() {
      @Override
      public void run() {
        GwtTour.endTour();
      }
    };
    GwtTour.startTour(tour);
    timer.schedule(timeout);
  }

  public static void closeOpenCallout() {
    GwtTour.endTour();
  }
  
}
