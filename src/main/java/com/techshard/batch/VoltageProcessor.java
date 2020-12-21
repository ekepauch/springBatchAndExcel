package com.techshard.batch;


import org.springframework.batch.item.ItemProcessor;

public class VoltageProcessor implements ItemProcessor<StudentDTO,StudentDTO >{

    @Override
    public StudentDTO process(final StudentDTO voltage) {
        final String emailAddress = voltage.getEmailAddress();
        final String name = voltage.getName();
        final String purchasedPackage = voltage.getPurchasedPackage();

        final StudentDTO processedVoltage = new StudentDTO();
        processedVoltage.setEmailAddress(emailAddress);
        processedVoltage.setName(name);
        processedVoltage.setPurchasedPackage(purchasedPackage);
        return processedVoltage;
    }

//    @Override
//    public StudentDTO process(StudentDTO voltage) throws Exception {
//        return voltage;
//    }
}
