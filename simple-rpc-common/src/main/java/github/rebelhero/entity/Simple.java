package github.rebelhero.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author rebelhero
 * @date 2020/11/25
 */
@Data
@Accessors(chain = true)
public class Simple {

    private String name;

    private String content;

}
