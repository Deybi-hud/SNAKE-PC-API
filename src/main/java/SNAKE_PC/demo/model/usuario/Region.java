package SNAKE_PC.demo.model.usuario;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "region")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Region {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_region", nullable = false, unique = true)
    private String nombreRegion;

    @OneToMany(mappedBy = "region")
    @JsonIgnore
    private List<Comuna> comunas;


}
