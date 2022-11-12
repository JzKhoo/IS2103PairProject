/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author khoojingzhi
 */
public class CarRentalReservationRecordNotFoundException extends Exception {

    /**
     * Creates a new instance of
     * <code>CarRentalReservationRecordNotFoundException</code> without detail
     * message.
     */
    public CarRentalReservationRecordNotFoundException() {
    }

    /**
     * Constructs an instance of
     * <code>CarRentalReservationRecordNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public CarRentalReservationRecordNotFoundException(String msg) {
        super(msg);
    }
}
