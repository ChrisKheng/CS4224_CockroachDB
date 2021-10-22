package cs4224.transactions;


import cs4224.dao.CustomerDao;
import cs4224.dao.DistrictDao;
import cs4224.dao.WarehouseDao;


public class PaymentTransaction extends BaseTransaction {

    private final WarehouseDao warehouseDao;
    private final DistrictDao districtDao;
    private final CustomerDao customerDao;

    public PaymentTransaction(WarehouseDao warehouseDao, DistrictDao districtDao, CustomerDao customerDao) {
        this.warehouseDao = warehouseDao;
        this.districtDao = districtDao;
        this.customerDao = customerDao;
    }

    @Override
    public void execute(String[] dataLines, String[] parameters) throws Exception {
        final int customerWarehouseId = Integer.parseInt(parameters[1]);
        final int customerDistrictId = Integer.parseInt(parameters[2]);
        final int customerId = Integer.parseInt(parameters[3]);
        final double paymentAmount = Double.parseDouble(parameters[4]);

        System.out.println(customerDao.getCustomerById(customerWarehouseId, customerDistrictId, customerId).toName());
    }
}
