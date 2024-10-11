package in.scrapeco.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CompanyType {

	PRIVATE_LIMITED("Private Limited"), PUBLIC_LIMITED("Public Limited"), PARTNERSHIP("Partnership");

	private final String displayName;

}
