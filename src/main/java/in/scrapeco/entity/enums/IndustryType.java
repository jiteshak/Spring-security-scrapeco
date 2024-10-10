package in.scrapeco.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum IndustryType {

	AUTOMOBILE("Automobile"), PACKAGING("Packaging"), TECHNOLOGY("Technology"), ELECTRICAL("Electrical"),
	PAPER("Paper"), METAL("Metal"), PLASTIC("Plastic"), PHARMACEUTICAL("Pharmaceutical"),
	MANUFACTURING("Manufacturing"), FOOD("Food"), OTHER("Other");

	private final String displayName;

}
