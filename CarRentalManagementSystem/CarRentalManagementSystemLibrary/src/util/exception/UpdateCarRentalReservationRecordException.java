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
public class UpdateCarRentalReservationRecordException extends Exception {

    /**
     * Creates a new instance of
     * <code>UpdateCarRentalReservationRecordException</code> without detail
     * message.
     */
    public UpdateCarRentalReservationRecordException() {
    }

    /**
     * Constructs an instance of
     * <code>UpdateCarRentalReservationRecordException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public UpdateCarRentalReservationRecordException(String msg) {
        super(msg);
    }
}
