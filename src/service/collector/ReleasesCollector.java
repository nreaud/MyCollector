package main.service.collector;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import main.model.Manga;
import main.model.Release;

/**
 * This Collector (for .collect()) can collect different releases into a map of
 * entries key=Manga, value=latest release
 * 
 * @author nicol
 *
 */
public class ReleasesCollector
		implements Collector<Release, Map<Manga, Release>, Map<Manga, Release>> {

	private ReleasesCollector() {
		// hidden
	}

	@Override
	public Supplier<Map<Manga, Release>> supplier() {
		return () -> new HashMap<Manga, Release>();
	}

	/**
	 * Add a new element, keep only one manga, the one with the most recent
	 * chapter number
	 */
	@Override
	public BiConsumer<Map<Manga, Release>, Release> accumulator() {
		return (map, newItem) -> {
			Manga newManga = newItem.getManga();
			if (!map.containsKey(newManga)) {
				map.put(newManga, newItem);
			} else {
				if (newItem.getNumber() > map.get(newManga).getNumber()) {
					map.put(newManga, newItem);
				}
			}
		};
	}

	@Override
	public BinaryOperator<Map<Manga, Release>> combiner() {
		return (left, right) -> {
			Map<Manga, Release> merged = new HashMap<>(left);
			right.entrySet().stream().forEach(entry -> {
				if (!merged.containsKey(entry.getKey())) {
					merged.put(entry.getKey(), entry.getValue());
				} else {
					if (entry.getValue().getNumber() > merged.get(entry.getKey())
							.getNumber()) {
						merged.put(entry.getKey(), entry.getValue());
					}
				}
			});
			return merged;
		};
	}

	@Override
	public Function<Map<Manga, Release>, Map<Manga, Release>> finisher() {
		return (list) -> list;
	}

	@Override
	public Set<Characteristics> characteristics() {
		Set<Characteristics> res = new HashSet<>();
		res.add(Characteristics.IDENTITY_FINISH);
		res.add(Characteristics.UNORDERED);
		return res;
	}

	public static ReleasesCollector getInstance() {
		return new ReleasesCollector();
	}

}
