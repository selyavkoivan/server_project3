package server.Models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "sortConfigurationBuilder")
public class SortConfiguration {
    private String SortColumn;
    private String SortValue;
    private int userId;
}
