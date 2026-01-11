package basic;

import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class streams8 {
    
    
    List<String> names = List.of("Andres", "Emilio", "Sofia", "Maria", "John");
    List<Integer> numbers = List.of(5, 10, 15, 20, 25);

    public void demostrateStreams(){

        names.sort(String::compareTo); // Sort using method reference
        names.sort((a, b) -> b.compareTo(a)); // Reverse sort using comparator
        System.out.println("Reverse Sorted Names: " + names);

        //Sorted example
        names.stream()
             .sorted()
             .forEach(System.out::println);
        

        // Example: Filter names starting with 'A' and print them
        names.stream()
             .filter(name -> name.startsWith("A"))
             .forEach(System.out::println);

        // Example: Map names to their lengths and collect to a list
        List<Integer> nameLengths = names.stream()
                                         .map(String::length)
                                         .toList();
        System.out.println("Name Lengths: " + nameLengths);

        // Example: Convert names to uppercase
        List<String> upperNames = names.stream()
                                      .map(String::toUpperCase)
                                      .collect(Collectors.toList()); // Collect to list
        
                                      System.out.println("Uppercase Names: " + upperNames);
        upperNames.add("Yes");
        // Example: Reduce to concatenate all names
        String concatenatedNames = names.stream()
                                        .reduce("", (a, b) -> a + ", " + b);
        System.out.println("Concatenated Names: " + concatenatedNames);

        Map<Integer, List<String>> groupedByLength = names.stream()
            .collect(Collectors.groupingBy(String::length));
        System.out.println("Grouped by Length: " + groupedByLength);

        //Intsummaring example
        IntSummaryStatistics stats = numbers.stream()
            .collect(Collectors.summarizingInt(Integer::intValue));
        stats.getMin();
        stats.getMax();
        stats.getAverage();
        stats.getSum();
        System.out.println("Statistics: " + stats);

        //Finding max and min
        int min = numbers.stream()
            .mapToInt(Integer::intValue)
            .min()
            .orElseThrow();
        System.out.println("Min: " + min);

    }


}
    