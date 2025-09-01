package dev.lonecalvary78.app.payment.internal.processing.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import dev.lonecalvary78.app.payment.internal.processing.model.dto.PaymentDTO;
import dev.lonecalvary78.app.payment.internal.processing.model.entity.Payment;

@Mapper
public interface PaymentMapper {
    PaymentMapper INSTANCE = Mappers.getMapper(PaymentMapper.class);

    @Mappings({
       @Mapping(source="id", target = "id"),
       @Mapping(source="debittedAccountNo", target = "debittedAccountNo"),
       @Mapping(source="creditedAccountNo", target="creditedAccountNo"),
       @Mapping(source="creditedAccountNo", target="creditedAccountNo"),
       @Mapping(source="paymentPurpose", target="paymentPurpose"),
       @Mapping(source="paymentReference", target="paymentReference"),
       @Mapping(source="status", target="status")        
    })
    Payment fromDTO(PaymentDTO paymentDTO);

    @Mappings({
       @Mapping(source="id", target = "id"),
       @Mapping(source="debittedAccountNo", target = "debittedAccountNo"),
       @Mapping(source="creditedAccountNo", target="creditedAccountNo"),
       @Mapping(source="creditedAccountNo", target="creditedAccountNo"),
       @Mapping(source="paymentPurpose", target="paymentPurpose"),
       @Mapping(source="paymentReference", target="paymentReference"),
       @Mapping(source="status", target="status")        
    })
    PaymentDTO toDTO(Payment payment);
}
