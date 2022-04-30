package tech.thatgravyboat.dashboard.constants;

import it.unimi.dsi.fastutil.ints.Int2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap;
import tech.thatgravyboat.dashboard.client.DifficultySwitcher;
import tech.thatgravyboat.dashboard.client.TimeSwitcher;
import tech.thatgravyboat.dashboard.client.WeatherSwitcher;

public class Constants {
	public static final String MODID = "dashboard";

	public static final Int2ObjectSortedMap<Switch> SWITCHERS = new Int2ObjectAVLTreeMap<>();

	static {
		SWITCHERS.put(294, new Switch("time", TimeSwitcher::new));
		SWITCHERS.put(295, new Switch("weather", WeatherSwitcher::new));
		SWITCHERS.put(296, new Switch("difficulty", DifficultySwitcher::new));
	}
}