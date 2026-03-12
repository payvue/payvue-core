package com.payvue.generic.payment.command;

import com.payvue.generic.payment.OrderItem;
import com.payvue.generic.payment.PaymentCommand;

public record VirtualAccountReserveCommand(OrderItem order, String bankCode) implements PaymentCommand {
}
