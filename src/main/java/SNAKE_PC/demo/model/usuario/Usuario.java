package SNAKE_PC.demo.model.usuario;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "usuario")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column (name ="usuario_imagen", columnDefinition = "TEXT", nullable = true )
    private String imagenUsuario;

    @Column(name = "nombre_usuario",nullable = false, unique = true)
    private String nombreUsuario;

    @Column(name = "correo", nullable = false, unique = true)
    private String correo;

    @Column(name = "contrasena",nullable = false)
    private String contrasena;    

    @Column(name = "activo")
    private boolean activo = true;

    @OneToOne(mappedBy = "usuario")
    @JsonIgnore
    private Contacto contacto;

    @ManyToOne
    @JoinColumn(name = "id_rol_usuario", nullable = true)
    private RolUsuario rolUsuario;

}
