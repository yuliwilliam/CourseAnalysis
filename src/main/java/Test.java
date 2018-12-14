import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Test {

    public static void main(String[] args) throws IOException {


        Document document = Jsoup.connect("http://coursefinder.utoronto.ca/course-search/search/courseSearch/coursedetails/LTE299Y1Y20189").get();

        Elements table = document.select("#u172 > tbody > tr");

        for (Element section : table) {

            Elements fields = section.select("td");

            String sectionName = fields.get(0).text();
            String lectureTime = fields.get(1).text();
            String instructor = fields.get(2).text();
            String location = fields.get(3).text();
            String courseSize = fields.get(4).text();
            String currentEnrolment = fields.get(5).text();
            String optionToWaitlist = fields.get(6).text();
            String deliveryMode = fields.get(7).text();

            System.out.println(fields.get(0).text());
        }


//        Elements element = document.getElementsByClass("dataTables_wrapper");

        //#u172 > tbody > tr:nth-child(4)

    }
}
