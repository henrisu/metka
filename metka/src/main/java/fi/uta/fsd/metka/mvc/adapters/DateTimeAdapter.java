package fi.uta.fsd.metka.mvc.adapters;

import org.joda.time.DateTime;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class DateTimeAdapter extends XmlAdapter<String, DateTime> {
    @Override
    public DateTime unmarshal(String v) throws Exception {
        return DateTime.parse(v);
    }

    @Override
    public String marshal(DateTime v) throws Exception {
        return v.toString();
    }
}
