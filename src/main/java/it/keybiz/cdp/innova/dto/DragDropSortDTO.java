package it.keybiz.cdp.innova.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DragDropSortDTO {
    @NotNull
    private UUID id;

    @NotNull
    @Min(1)
    private Integer position;
}
