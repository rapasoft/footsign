package ch.erni.community.footsign.util;

import ch.erni.community.footsign.exception.PropertyFileNotFound;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * User: ban
 * Date: 3. 3. 2015
 * Time: 13:02
 */

public class PropertyLoaderTest {

	private PropertyLoader propertyLoader;

	@Before
	public void setUp() {
		propertyLoader = new PropertyLoader();
	}

	@Test
	public void loadExistedProperty() throws Exception {
		String property = propertyLoader.getProperty(PropertyConstatns.MAVEN_PHOTO_DIR_PROPERTY);
		Assert.assertNotNull(property);
	}

	@Test
	public void loadNoneExistedProperty() throws Exception {
		String property = propertyLoader.getProperty("noneExistedPropety");
		Assert.assertNull(property);
	}

	@Test(expected = PropertyFileNotFound.class)
	public void loadPropertyFromNoneExistedFile() throws PropertyFileNotFound {

		propertyLoader.getProperty(PropertyConstatns.MAVEN_PHOTO_DIR_PROPERTY, "noneExistedFile");
	}

}
