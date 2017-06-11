package org.diveintojee.poc.vintagezerodowntime.engineserver.api;

import org.diveintojee.poc.vintagezerodowntime.dto.MeasurementFactDTO;
import org.diveintojee.poc.vintagezerodowntime.engineserver.service.MeasurementFactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by <a href="mailto:louis.gueye@domo-safety.com">Louis Gueye</a>.
 */
@RestController
@RequestMapping("/api/facts")
public class MeasurementFactsResource {
	@Autowired
	private MeasurementFactService service;

	/**
	 * Finds heart rate facts by criteria
	 * @param criteria
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = {MediaType.APPLICATION_JSON_VALUE})
	public List<MeasurementFactDTO> search(@ModelAttribute MeasurementFactDTO criteria) {
		return service.findByCriteria(criteria);
	}

	/**
	 * Fully loads heart rate fact
	 * @param businessId
	 * @return
	 */
	@RequestMapping(value = "{businessId}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
	public MeasurementFactDTO load(@PathVariable("businessId") String businessId) {
		return service.load(businessId);
	}

}
