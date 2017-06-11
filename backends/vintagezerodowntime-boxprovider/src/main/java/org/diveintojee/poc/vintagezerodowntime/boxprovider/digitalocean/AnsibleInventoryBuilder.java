package org.diveintojee.poc.vintagezerodowntime.boxprovider.digitalocean;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.diveintojee.poc.vintagezerodowntime.boxprovider.digitalocean.domain.Box;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by <a href="mailto:louis.gueye@domo-safety.com">Louis Gueye</a>.
 */
@Component
public class AnsibleInventoryBuilder {

//	private static final Logger LOGGER = LoggerFactory.getLogger(AnsibleInventoryBuilder.class);

	public String build(List<Box> boxes) throws IOException {
		Map<String, List<Box>> sections = Maps.newTreeMap();
		for (Box box : boxes) {
			final List<String> tokens = Lists.newArrayList(Splitter.on("-").split(box.getName()));
			String env = tokens.get(0);
			String role = tokens.get(1);
			sections.computeIfAbsent(env, k -> Lists.newArrayList());
			sections.get(env).add(box);
			sections.computeIfAbsent(role, k -> Lists.newArrayList());
			sections.get(role).add(box);
		}

		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, List<Box>> entry : sections.entrySet()) {
			final String section = entry.getKey();
			sb.append("[").append(section).append("]\n");
			for (Box box : entry.getValue()) {
				final String ip = box.getIps().get(0);
				sb.append(ip).append("\n");
			}
			sb.append("\n");
		}

		return sb.toString();
	}

}
