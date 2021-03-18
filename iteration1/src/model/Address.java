package model;

import domainapp.basics.exceptions.ConstraintViolationException;
import domainapp.basics.model.meta.AttrRef;
import domainapp.basics.model.meta.DAssoc;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DClass;
import domainapp.basics.model.meta.DOpt;
import domainapp.basics.util.Tuple;

@DClass(schema="hotelsystem")
public class Address {

    public static final String A_name = "name";

    @DAttr(name="id",id=true,auto=true,length=3,mutable=false,optional=false,type= DAttr.Type.Integer)
    private int id;
    private static int idCounter;

    @DAttr(name=A_name,type= DAttr.Type.String,length=20,optional=false)
    private String name;

    @DAttr(name="person",type= DAttr.Type.Domain,serialisable=false)
    @DAssoc(ascName="person-has-address",role="address",
            ascType= DAssoc.AssocType.One2One, endType= DAssoc.AssocEndType.One,
            associate=@DAssoc.Associate(type= Person.class,cardMin=1,cardMax=1,determinant=true))
    private Person person;

    // from object form: Student is not included
    @DOpt(type=DOpt.Type.ObjectFormConstructor)
    @DOpt(type=DOpt.Type.RequiredConstructor)
    public Address(@AttrRef("name") String cityName) {
        this(null, cityName, null);
    }

    // from object form: Student is included
    @DOpt(type=DOpt.Type.ObjectFormConstructor)
    public Address(@AttrRef("name") String cityName, @AttrRef("person") Person person) {
        this(null, cityName, person);
    }

    // from data source
    @DOpt(type=DOpt.Type.DataSourceConstructor)
    public Address(@AttrRef("id") Integer id, @AttrRef("name") String cityName) {
        this(id, cityName, null);
    }

    // based constructor (used by others)
    private Address(Integer id, String cityName, Person person) {
        this.id = nextId(id);
        this.name = cityName;
        this.person = person;
    }

    private static int nextId(Integer currID) {
        if (currID == null) {
            idCounter++;
            return idCounter;
        } else {
            int num = currID.intValue();
            if (num > idCounter)
                idCounter = num;

            return currID;
        }
    }

    /**
     * @requires
     *  minVal != null /\ maxVal != null
     * @effects
     *  update the auto-generated value of attribute <tt>attrib</tt>, specified for <tt>derivingValue</tt>, using <tt>minVal, maxVal</tt>
     */
    @DOpt(type=DOpt.Type.AutoAttributeValueSynchroniser)
    public static void updateAutoGeneratedValue(
            DAttr attrib,
            Tuple derivingValue,
            Object minVal,
            Object maxVal) throws ConstraintViolationException {

        if (minVal != null && maxVal != null) {
            //TODO: update this for the correct attribute if there are more than one auto attributes of this class
            int maxIdVal = (Integer) maxVal;
            if (maxIdVal > idCounter)
                idCounter = maxIdVal;
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Person getPerson() {
        return person;
    }

    @DOpt(type=DOpt.Type.LinkAdderNew)
    public void setNewPerson(Person person) {
        this.person = person;
        // do other updates here (if needed)
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @Override
    public String toString() {
        return name;
    }
}

