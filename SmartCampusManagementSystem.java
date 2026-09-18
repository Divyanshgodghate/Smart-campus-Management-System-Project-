package smartcampus;
import java.time.LocalDate;
import java.util.*;

public class SmartCampusManagementSystem {
 private final Scanner in = new Scanner(System.in);
 private final Map<String,Student> students = new LinkedHashMap<>();
 private final Map<String,Course> courses = new LinkedHashMap<>();
 private final Map<String,Set<String>> enrollments = new HashMap<>();
 private final Map<String,Map<LocalDate,Set<String>>> attendance = new HashMap<>();

 public SmartCampusManagementSystem(){ seedData(); }
 public void run(){ while(true){ System.out.println("\n=== SMART CAMPUS MANAGEMENT SYSTEM ===");
  System.out.println("1. Add student\n2. Add course\n3. Enroll student\n4. Mark attendance");
  System.out.println("5. View student details\n6. View course roster\n7. Campus report\n0. Exit");
  switch(read("Choose an option: ")){case "1"->addStudent();case "2"->addCourse();case "3"->enroll();case "4"->markAttendance();case "5"->showStudent();case "6"->showRoster();case "7"->report();case "0"->{System.out.println("Goodbye.");return;}default->System.out.println("Invalid option.");}}}
 private void addStudent(){String id=read("Student ID: ");if(students.containsKey(id)){System.out.println("That ID already exists.");return;}students.put(id,new Student(id,read("Name: "),read("Email: "),read("Department: ")));System.out.println("Student added.");}
 private void addCourse(){String code=read("Course code: ").toUpperCase();if(courses.containsKey(code)){System.out.println("That course already exists.");return;}try{int cap=Integer.parseInt(read("Capacity: "));if(cap<=0)throw new NumberFormatException();courses.put(code,new Course(code,read("Title: "),read("Instructor: "),cap));System.out.println("Course added.");}catch(NumberFormatException e){System.out.println("Capacity must be a positive whole number.");}}
 private void enroll(){String id=read("Student ID: "),code=read("Course code: ").toUpperCase();if(!students.containsKey(id)||!courses.containsKey(code)){System.out.println("Student or course was not found.");return;}Set<String> roster=enrollments.computeIfAbsent(code,k->new LinkedHashSet<>());if(roster.size()>=courses.get(code).capacity()&&!roster.contains(id)){System.out.println("Course is full.");return;}System.out.println(roster.add(id)?"Enrollment successful.":"Student is already enrolled.");}
 private void markAttendance(){String code=read("Course code: ").toUpperCase();if(!courses.containsKey(code)){System.out.println("Course not found.");return;}Set<String> roster=enrollments.getOrDefault(code,Set.of());if(roster.isEmpty()){System.out.println("No enrolled students.");return;}Set<String> present=new LinkedHashSet<>();for(String id:roster)if(read(students.get(id).name()+" ("+id+"): ").equalsIgnoreCase("P"))present.add(id);attendance.computeIfAbsent(code,k->new TreeMap<>()).put(LocalDate.now(),present);System.out.println("Attendance saved.");}
 private void showStudent(){String id=read("Student ID: ");Student s=students.get(id);if(s==null){System.out.println("Student not found.");return;}System.out.printf("%s | %s | %s | %s%nEnrolled courses:%n",s.id(),s.name(),s.email(),s.department());courses.keySet().stream().filter(c->enrollments.getOrDefault(c,Set.of()).contains(id)).forEach(c->System.out.println("- "+c+": "+courses.get(c).title()));}
 private void showRoster(){String code=read("Course code: ").toUpperCase();Course c=courses.get(code);if(c==null){System.out.println("Course not found.");return;}System.out.printf("%s — %s (%d/%d)%n",c.code(),c.title(),enrollments.getOrDefault(code,Set.of()).size(),c.capacity());enrollments.getOrDefault(code,Set.of()).forEach(id->System.out.println("- "+students.get(id).name()+" ["+id+"]"));}
 private void report(){System.out.printf("Students: %d | Courses: %d | Enrollments: %d%n",students.size(),courses.size(),enrollments.values().stream().mapToInt(Set::size).sum());courses.keySet().forEach(code->{var log=attendance.getOrDefault(code,Map.of());int held=log.size(),present=log.values().stream().mapToInt(Set::size).sum(),possible=held*enrollments.getOrDefault(code,Set.of()).size();System.out.printf("%s: %d enrolled, attendance %.1f%% (%d sessions)%n",code,enrollments.getOrDefault(code,Set.of()).size(),possible==0?0:100.0*present/possible,held);});}
 private String read(String p){System.out.print(p);return in.nextLine().trim();}
 private void seedData(){students.put("S001",new Student("S001","Aisha Khan","aisha@campus.edu","Computer Science"));students.put("S002",new Student("S002","Rahul Mehta","rahul@campus.edu","Engineering"));courses.put("CS101",new Course("CS101","Programming Fundamentals","Dr. Sharma",40));courses.put("MA201",new Course("MA201","Discrete Mathematics","Dr. Iyer",35));}
}