package io.github.sskorol.testcases;

import io.github.sskorol.dataprovider.DataSupplier;
import io.github.sskorol.datasuppliers.ExternalDataSuppliers;
import io.github.sskorol.model.User;
import one.util.streamex.StreamEx;
import org.testng.annotations.Test;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;

public class StreamsDataSupplierTests {

	@DataSupplier
	public Stream<Integer> getPrimitiveStreamData() {
		return IntStream.range(0, 10).filter(d -> d % 2 == 0).boxed();
	}

	@DataSupplier(extractValues = true)
	public Stream<User> extractCustomStreamData() {
		return Stream
				.of(new User("Petya", "password2"),
						new User("Virus Petya", "password3"), new User("Mark", "password1"))
				.filter(u -> !u.getName().contains("Virus"))
				.sorted(comparing(User::getPassword));
	}

	@DataSupplier
	public StreamEx<String> getCustomStreamData() {
		return StreamEx.of(new User("user2", "password2"),
				new User("user3", "password3"), new User("user1", "password1"))
				.map(User::getName).sorted().skip(1);
	}

	@Test(dataProvider = "getPrimitiveStreamData")
	public void supplyPrimitiveStreamData(final int ob) {
		// not implemented
	}

	@Test(dataProvider = "extractCustomStreamData")
	public void supplyExtractedCustomStreamData(final User user1,
			final User user2) {
		// not implemented
	}

	@Test(dataProvider = "getCustomStreamData")
	public void supplyCustomStreamData(final String ob) {
		// not implemented
	}

	@Test(dataProviderClass = ExternalDataSuppliers.class, dataProvider = "getExternalStreamData")
	public void supplyExternalStreamData(final long ob) {
		// not implemented
	}
}
