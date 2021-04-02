package hanu.edu.hotelsystem.services.ServiceOrder.model;

import domainapp.basics.exceptions.ConstraintViolationException;
import domainapp.basics.model.meta.AttrRef;
import domainapp.basics.model.meta.DAssoc;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DClass;
import domainapp.basics.model.meta.DOpt;
import hanu.edu.hotelsystem.services.Person.model.Employee;
import hanu.edu.hotelsystem.services.Reservation.model.Reservation;
import hanu.edu.hotelsystem.services.Service.model.TransportationService.TransportationService;

import java.util.Date;

@DClass(schema = "hotelsystem" )
public class TransportationServiceOrder extends ServiceOrder{
    public static final String C_code = "code";

    @DAttr(name = C_code, type = DAttr.Type.String, auto = true, length = 6,
            mutable = false, optional = false)
    private String code;
    private static int counter;

    @DAttr(name="transportationService",type= DAttr.Type.Domain,length = 30, optional = false)
    @DAssoc(ascName="transportation-service-has-transportation-service-order",role="transportation-service-order",
            ascType= DAssoc.AssocType.One2Many,endType= DAssoc.AssocEndType.Many,
            associate=@DAssoc.Associate(type= TransportationService.class,cardMin=1,cardMax=1))
    private TransportationService transportationService;

    @DAttr(name = "distance", type = DAttr.Type.Integer, optional = false, min = 1)
    private Integer distance;

    @DOpt(type = DOpt.Type.ObjectFormConstructor)
    public TransportationServiceOrder(@AttrRef("createdAt") Date createdAt,
                           @AttrRef("roomService") TransportationService transportationService,
                           @AttrRef("distance") Integer distance,
                           @AttrRef("quantity") Integer quantity,
                           @AttrRef("reservation") Reservation reservation,
                           @AttrRef("employee") Employee employee
    ){
        this(null, createdAt,transportationService,distance, quantity, reservation,employee);
    }

    @DOpt(type=DOpt.Type.DataSourceConstructor)
    public TransportationServiceOrder(@AttrRef("code") String code,
                                      @AttrRef("createdAt") Date createdAt,
                                      @AttrRef("transportationService") TransportationService transportationService,
                                      @AttrRef("distance") Integer distance,
                                      @AttrRef("quantity") Integer quantity,
                                      @AttrRef("reservation") Reservation reservation,
                                      @AttrRef("employee") Employee employee

    ) throws ConstraintViolationException {
        super(createdAt, quantity, reservation, employee);
        this.code = nextCode(code);
        this.transportationService = transportationService;
        this.distance = distance;
    }

    public String getCode() {
        return code;
    }

    private String nextCode(String code) throws ConstraintViolationException {
        if (code == null) { // generate a new id
            return "TS" + ++counter;
        } else {
            // update id
            int num;
            try {
                num = Integer.parseInt(code.substring(1));
            } catch (RuntimeException e) {
                throw new ConstraintViolationException(
                        ConstraintViolationException.Code.INVALID_VALUE, e, new Object[] { code});
            }
            if (num > counter) {
                counter = num;
            }
            return code;
        }
    }

    public TransportationService getTransportationService() {
        return transportationService;
    }

    public void setTransportationService(TransportationService transportationService) {
        this.transportationService = transportationService;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }
}