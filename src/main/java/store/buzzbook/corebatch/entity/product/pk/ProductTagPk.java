package store.buzzbook.corebatch.entity.product.pk;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@AllArgsConstructor
@Embeddable
@EqualsAndHashCode
public class ProductTagPk {

	private int productId;
	private int tagId;
}
