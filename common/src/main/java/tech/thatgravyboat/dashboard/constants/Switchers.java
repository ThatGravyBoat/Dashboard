package tech.thatgravyboat.dashboard.constants;

import com.mojang.blaze3d.platform.InputConstants;
import it.unimi.dsi.fastutil.ints.Int2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap;
import tech.thatgravyboat.dashboard.client.DifficultySwitcher;
import tech.thatgravyboat.dashboard.client.TimeSwitcher;
import tech.thatgravyboat.dashboard.client.WeatherSwitcher;

public final class Switchers {

	private static final Int2ObjectSortedMap<Switch> SWITCHERS = new Int2ObjectAVLTreeMap<>();

	static {
		register(InputConstants.KEY_F5, new Switch("time", TimeSwitcher::new));
		register(InputConstants.KEY_F6, new Switch("weather", WeatherSwitcher::new));
		register(InputConstants.KEY_F7, new Switch("difficulty", DifficultySwitcher::new));
	}

	public static void register(int key, Switch switcher) {
		if (SWITCHERS.containsKey(key)) {
			throw new IllegalArgumentException("Switcher already registered for key: " + key);
		}
		SWITCHERS.put(key, switcher);
	}

	public static Switch get(int key) {
		return SWITCHERS.get(key);
	}
}