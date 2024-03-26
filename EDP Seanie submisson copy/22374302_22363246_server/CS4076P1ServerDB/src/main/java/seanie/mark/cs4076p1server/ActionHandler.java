package seanie.mark.cs4076p1server;

public class ActionHandler {
    static String addClass(String details) {
        int numberOfModules = UtilityFunctions.getNumberOfModules();
        if(numberOfModules <= 5){
            String[] parts = details.strip().split(" ");

            String moduleCode = parts[0];
            String time = parts[1];
            String day = parts[2];
            String room = parts[3];

            boolean overlapping = UtilityFunctions.checkOverlap(time, day);
            if(overlapping){
                return "ol";
            }

            boolean validModule = UtilityFunctions.isValidModule(moduleCode);
            if (!validModule) {
                return "im";
            }

            boolean validRoom = UtilityFunctions.isValidRoom(room);
            if (!validRoom) {
                return "ir";
            }

            boolean moduleInDB = UtilityFunctions.moduleInDB(moduleCode);
            if(moduleInDB){
                UtilityFunctions.addTimetableEntryToDB(moduleCode, time, day, room);
            }
            else{
                UtilityFunctions.addModuleToDB(moduleCode);
                UtilityFunctions.addTimetableEntryToDB(moduleCode, time, day, room);
            }
            return "ca";

        }
        else {
            return "ttf";
        }
    }

    static String removeClass(String details) {
        String[] parts = details.strip().split(" ");

        String moduleCode = parts[0];
        String time = parts[1];
        String day = parts[2];
        String room = parts[3];

        boolean moduleInDB = UtilityFunctions.moduleInDB(moduleCode);
        if(moduleInDB){
            return UtilityFunctions.removeTimetableEntryFromDB(moduleCode, time, day, room);
        }
        else{
            return "cnf";
        }
    }

    static String displaySchedule(String details) {
        String[] parts = details.strip().split(" ");

        String moduleCode = parts[0];

        boolean moduleInDB = UtilityFunctions.moduleInDB(moduleCode);
        if(moduleInDB){
            return UtilityFunctions.displayModuleTimetable(moduleCode);
        }
        else{
            return "cnf";
        }
    }

}
