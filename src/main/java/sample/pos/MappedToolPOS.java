package sample.pos;

import sample.pos.domain.CheckinRequest;
import sample.pos.domain.RentalAgreement;
import sample.pos.handlers.Handler;
import sample.pos.processors.Processor;
import sample.pos.renderers.Renderer;

/**
 * POS-Handler used with an in-house tool repository consisting of a local Tool Hashmap.
 * <p>
 * Processing of RentalAgreements equates to printing them out on the screen. No RentalAgreement persistence
 * takes place.
 */
public class MappedToolPOS implements Handler<CheckinRequest, RentalAgreement>, Processor<RentalAgreement> {


    /**
     * Handles conversion of RentalAgreement to a printable string
     */
    private final Renderer<RentalAgreement> renderer;

    /**
     * Delegate for handling the checkout/handler logic
     */
    private final Handler<CheckinRequest, RentalAgreement> handlerDelegate;

    public MappedToolPOS(Renderer<RentalAgreement> renderer,
                         Handler<CheckinRequest, RentalAgreement> handlerDelegate) {
        this.renderer = renderer;
        this.handlerDelegate = handlerDelegate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RentalAgreement handle(CheckinRequest checkinRequest) throws Exception {
        return handlerDelegate.handle(checkinRequest);
    }

    /**
     * Prints out rental agreement to standard output.
     * <p>
     * Relies on {@link Renderer} object to render rental agreement in proper format
     *
     * @param rentalAgreement Item to print to standard output
     */
    @Override
    public void process(RentalAgreement rentalAgreement) {
        System.out.println(renderer.render(rentalAgreement));
    }
}
