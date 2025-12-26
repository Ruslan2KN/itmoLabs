package lab3.mainStory;

import lab3.characters.Dwarf;
import lab3.world.*;
import lab3.world.playgrounds.*;

public class Strory {
    public static void main(String[] args) {
        House house1 = new House("Улица Карликов, дом 1", 1, new Theater("Большой театр", 150));
        House house2 = new House("Улица Карликов, дом 2", 2, new Cinema("World of Warcraft", 100));

        Courtyard courtyard1 = new Courtyard(1);
        courtyard1.addHouse(house1);
        courtyard1.addHouse(house2);

        Dwarf dwarf1 = new Dwarf("Карлика Kudoth Oddbelch", 10.0, 50.0, house1);
        Dwarf dwarf2 = new Dwarf("Карлика Okignugs Hammercrag", 15.0, 60.0, house2);

        Tennis tennisCourt = new Tennis(5);
        Basketball basketballCourt = new Basketball(true, false);
        Volleyball volleyballCourt = new Volleyball(true);
        Swimming swimmingPool = new Swimming(25.0);

        courtyard1.addPlayground(tennisCourt);
        courtyard1.addPlayground(basketballCourt);
        courtyard1.addPlayground(volleyballCourt);
        courtyard1.addPlayground(swimmingPool);

        System.out.println("Коротышки во дворе наслаждаются игрой и отдыхом на различных площадках");

        // дворфы играют на разных площадках + тесты на закрытость площадки
        dwarf1.play(tennisCourt);
        tennisCourt.breakRacket();
        dwarf1.play(tennisCourt);
        dwarf2.play(basketballCourt);
        basketballCourt.change3PointShotLine();
        dwarf2.play(basketballCourt);

        //System.out.println("Карлики переходят к волейбольной площадке");
        dwarf1.play(volleyballCourt);
        dwarf2.play(volleyballCourt);


        //System.out.println("Теперь коротышки отдыхают в плавательном бассейне");
        dwarf1.play(swimmingPool);
        dwarf2.play(swimmingPool);

        //System.out.println("Карлики переходят в дома с театрами для отдыха");
        courtyard1.moveDwarfToNewHouse(dwarf1, dwarf2);

       // System.out.println("Карлики наслаждаются просмотром спектаклей!");
        System.out.println(house1.getTheater().describe());
        house1.getTheater().switchViewMode();
        System.out.println(house1.getTheater().describe());
        System.out.println(house2.getCinema().describe());

    }

}

