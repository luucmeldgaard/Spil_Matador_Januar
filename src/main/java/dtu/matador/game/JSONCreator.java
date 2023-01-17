package dtu.matador.game;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class JSONCreator {

    public static void main(String[] args) throws IOException {

        Scanner scan = new Scanner(System.in);
        JSONArray fieldList = new JSONArray();

        String lastScannedLine = "";
        int iteration = 0;
        while (!lastScannedLine.equals("end")) {
            JSONObject fieldDetails = new JSONObject();
            ArrayList<String> allowableFieldTypes = new ArrayList<>();
            allowableFieldTypes.add("p");
            allowableFieldTypes.add("c");
            allowableFieldTypes.add("j");
            allowableFieldTypes.add("t");
            allowableFieldTypes.add("s");
            allowableFieldTypes.add("f");
            System.out.println("Choose type: P(roperty), C(hance), J(ail), T(ax), S(tart), F(ree-parking)");
            System.out.print("FieldType: ");
            String fieldType = scan.nextLine();
            for (int i = 0; i < 50; ++i) System.out.println();
            if (allowableFieldTypes.contains(fieldType)) {
                System.out.print("[" + iteration + "]" + "FieldType: ");
                switch (fieldType) {
                    case "p" -> {
                        System.out.println("Property");
                        fieldDetails.put("fieldType", "property");
                        System.out.print("rent: ");
                        fieldDetails.put("rent", scan.nextLine());
                        System.out.print("price: ");
                        fieldDetails.put("price", scan.nextLine());
                        System.out.print("Neighborhood: ");
                        fieldDetails.put("neighborhood", scan.nextLine());
                        System.out.print("buildPrice: ");
                        fieldDetails.put("buildPrice", scan.nextLine());
                        System.out.print("pawnForAmount: ");
                        fieldDetails.put("pawnForAmount", scan.nextLine());
                        System.out.print("Owner: ");
                        fieldDetails.put("owner", scan.nextLine());
                    }
                    case "c" -> {
                        System.out.println("Chance");
                        fieldDetails.put("fieldType", "chance");
                        System.out.print("actionText: ");
                        fieldDetails.put("actionText", scan.nextLine());
                        System.out.print("action; moveTo, tax, reward, getOutOfJailCard: ");
                        fieldDetails.put("actionType", scan.nextLine());
                    }
                    case "j" -> {
                        System.out.println("Jail");
                        fieldDetails.put("fieldType", "jail");
                    }
                    case "t" -> {
                        System.out.println("Tax");
                        fieldDetails.put("fieldType", "tax");
                        System.out.print("taxationRate: ");
                        fieldDetails.put("taxationRate", scan.nextLine());
                    }
                    case "s" -> {
                        System.out.println("Start");
                        fieldDetails.put("fieldType", "start");
                        System.out.print("bonus: ");
                        fieldDetails.put("bonus", scan.nextInt());
                    }
                    case "f" -> {
                        System.out.println("Free-Parking");
                        fieldDetails.put("fieldType", "free-parking");
                    }
                }

                fieldDetails.put("position", String.valueOf(iteration));
                System.out.print("title: ");
                fieldDetails.put("title", scan.nextLine());
                System.out.print("subText: ");
                fieldDetails.put("subText", scan.nextLine());
                System.out.print("Color 1: ");
                fieldDetails.put("color1", scan.nextLine());
                System.out.print("Color 2: ");
                fieldDetails.put("color2", scan.nextLine());

                fieldList.add(fieldDetails);

                for (int i = 0; i < 50; ++i) System.out.println();

                iteration += 1;
            }

            System.out.println("Press enter to continue or type 'end' to save and exit");
            lastScannedLine = scan.nextLine();
            for (int i = 0; i < 50; ++i) System.out.println();
        }

        JSONObject testFieldList = new JSONObject();
        testFieldList.put("board", fieldList);

        try (FileWriter fieldFile = new FileWriter("fieldSpaces.json")) {
            //fieldFile.write(fieldList.toJSONString());
            fieldFile.write(fieldList.toJSONString());
            fieldFile.flush();
        } catch (
                IOException ex) {
            throw new RuntimeException(ex);
        }

    }
}
