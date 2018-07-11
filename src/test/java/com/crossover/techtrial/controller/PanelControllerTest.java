package com.crossover.techtrial.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.crossover.techtrial.RandomGenerator;
import com.crossover.techtrial.dto.DailyElectricity;
import com.crossover.techtrial.model.HourlyElectricity;
import com.crossover.techtrial.model.Panel;
import com.crossover.techtrial.service.HourlyElectricityService;
import com.crossover.techtrial.service.PanelService;

/**
 * PanelControllerTest class will test all APIs in PanelController.java.
 * 
 * @author Crossover
 *
 */

@RunWith(SpringRunner.class)
@WebMvcTest(PanelController.class)
@EnableSpringDataWebSupport
public class PanelControllerTest {

  private static final DateTimeFormatter jsonDateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
  private static final DateTimeFormatter jsonDateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;

  @Autowired
  private MockMvc mvc;

  @MockBean
  private PanelService panelService;

  @MockBean
  private HourlyElectricityService hourlyElectricityService;

  @Test
  public void givenValidSerial_whenGetDailyElectricity_thenReturnJsonStructure()
      throws Exception {

    final Panel panel = new Panel(RandomGenerator.generatedId(),
	RandomGenerator.randomPanelSerial(),
	RandomGenerator.randomLongitude(),
	RandomGenerator.randomLatitude(),
	"Solar inc.");

    when(panelService.findBySerial(panel.getSerial())).thenReturn(panel);

    final List<DailyElectricity> result = Arrays.asList(
	new DailyElectricity(LocalDate.now(), RandomGenerator.generatedElectricitySummaryStatistics()),
	new DailyElectricity(LocalDate.now().minusDays(1), RandomGenerator.generatedElectricitySummaryStatistics()),
	new DailyElectricity(LocalDate.now().minusDays(2), RandomGenerator.generatedElectricitySummaryStatistics()));

    when(hourlyElectricityService.getPanelDailyStatistics(panel.getId())).thenReturn(result);

    mvc.perform(get("/api/panels/" + panel.getSerial() + "/daily"))
	.andExpect(status().isOk())
	.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
	.andExpect(jsonPath("$", hasSize(3)))
	.andExpect(jsonPath("$[0].average", is(result.get(0).getAverage().doubleValue())))
	.andExpect(jsonPath("$[0].max", is(result.get(0).getMax().intValue())))
	.andExpect(jsonPath("$[0].min", is(result.get(0).getMin().intValue())))
	.andExpect(jsonPath("$[0].sum", is(result.get(0).getSum().intValue())))
	.andExpect(jsonPath("$[0].date", is(result.get(0).getDate().format(jsonDateFormatter))))
	.andExpect(jsonPath("$[1].average", is(result.get(1).getAverage().doubleValue())))
	.andExpect(jsonPath("$[1].max", is(result.get(1).getMax().intValue())))
	.andExpect(jsonPath("$[1].min", is(result.get(1).getMin().intValue())))
	.andExpect(jsonPath("$[1].sum", is(result.get(1).getSum().intValue())))
	.andExpect(jsonPath("$[1].date", is(result.get(1).getDate().format(jsonDateFormatter))))
	.andExpect(jsonPath("$[2].average", is(result.get(2).getAverage().doubleValue())))
	.andExpect(jsonPath("$[2].max", is(result.get(2).getMax().intValue())))
	.andExpect(jsonPath("$[2].min", is(result.get(2).getMin().intValue())))
	.andExpect(jsonPath("$[2].sum", is(result.get(2).getSum().intValue())))
	.andExpect(jsonPath("$[2].date", is(result.get(2).getDate().format(jsonDateFormatter))))
	.andDo(print());
  }

  @Test
  public void givenInvalidSerial_whenGetDailyElectricity_thenReturnNotFound() throws Exception {
    mvc.perform(get("/api/panels/ABC/daily"))
	.andExpect(status().isNotFound())
	.andDo(print());
  }

  @Test
  public void givenValidSerial_whenGetHourlyElectricity_thenReturnJsonStructure()
      throws Exception {

    final Panel panel = new Panel(RandomGenerator.generatedId(),
	RandomGenerator.randomPanelSerial(),
	RandomGenerator.randomLongitude(),
	RandomGenerator.randomLatitude(),
	"Solar inc.");

    final HourlyElectricity he0 = new HourlyElectricity(RandomGenerator.generatedId(), panel,
	RandomGenerator.generatedElectricity(),
	RandomGenerator.randomLocalDateTime());
    final HourlyElectricity he1 = new HourlyElectricity(RandomGenerator.generatedId(), panel,
	RandomGenerator.generatedElectricity(),
	RandomGenerator.randomLocalDateTime());

    final Page<HourlyElectricity> result = new PageImpl<>(Arrays.asList(he0, he1), PageRequest.of(0, 10), 0);

    when(panelService.findBySerial(panel.getSerial())).thenReturn(panel);
    when(hourlyElectricityService.getAllHourlyElectricityByPanelId(eq(panel.getId()), any(Pageable.class)))
	.thenReturn(result);

    mvc.perform(get("/api/panels/" + panel.getSerial() + "/hourly"))
	.andExpect(status().isOk())
	.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
	.andExpect(jsonPath("$.pageable", not(empty())))
	.andExpect(jsonPath("$.content", hasSize(2)))
	.andExpect(jsonPath("$.content[0].id", is(he0.getId().intValue())))
	.andExpect(jsonPath("$.content[0].generatedElectricity", is(he0.getGeneratedElectricity().intValue())))
	.andExpect(jsonPath("$.content[0].readingAt", is(he0.getReadingAt().format(jsonDateTimeFormatter))))
	.andExpect(jsonPath("$.content[1].id", is(he1.getId().intValue())))
	.andExpect(jsonPath("$.content[1].generatedElectricity", is(he1.getGeneratedElectricity().intValue())))
	.andExpect(jsonPath("$.content[1].readingAt", is(he1.getReadingAt().format(jsonDateTimeFormatter))))
	.andDo(print());
  }

  @Test
  public void givenInvalidSerial_whenGetHourlyElectricity_thenReturnNotFound()
      throws Exception {
    mvc.perform(get("/api/panels/ABC/hourly"))
	.andExpect(status().isNotFound())
	.andDo(print());
  }

  @Test
  public void givenNewHourlyElectricityAndValidSerial_whenSaveHourlyElectricity_thenReturnJsonStructure()
      throws Exception {

    final Panel panel = new Panel(RandomGenerator.generatedId(),
	RandomGenerator.randomPanelSerial(),
	RandomGenerator.randomLongitude(),
	RandomGenerator.randomLatitude(),
	"Solar inc.");

    final HourlyElectricity he = new HourlyElectricity(null, RandomGenerator.generatedElectricity(),
	RandomGenerator.randomLocalDateTime());
    final HourlyElectricity heSaved = new HourlyElectricity(RandomGenerator.generatedId(), null,
	he.getGeneratedElectricity(), he.getReadingAt());

    when(panelService.findBySerial(panel.getSerial())).thenReturn(panel);
    when(hourlyElectricityService.save(he)).thenReturn(heSaved);

    mvc.perform(post("/api/panels/" + panel.getSerial() + "/hourly")
	.contentType(MediaType.APPLICATION_JSON_UTF8)
	.content("{\"generatedElectricity\":" + he.getGeneratedElectricity() + ",\"readingAt\":\""
	    + he.getReadingAt().toString() + "\" }"))
	.andExpect(status().isOk())
	.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
	.andExpect(jsonPath("$.id", equalTo(heSaved.getId().intValue())))
	.andExpect(jsonPath("$.generatedElectricity", is(heSaved.getGeneratedElectricity().intValue())))
	.andExpect(jsonPath("$.readingAt", is(heSaved.getReadingAt().format(jsonDateTimeFormatter))))
	.andDo(print());
  }

  @Test
  public void givenNewHourlyElectricityAndWrongSerial_whenSaveHourlyElectricity_thenReturnNotFound() throws Exception {
    mvc.perform(post("/api/panels/ABC/hourly")
	.contentType(MediaType.APPLICATION_JSON_UTF8)
	.content("{\"generatedElectricity\":123456,\"readingAt\":\"2018-07-11T12:30:00\" }"))
	.andExpect(status().isNotFound())
	.andDo(print());
  }

  @Test
  public void givenNewPanel_whenRegisterPanel_thenReturnAccepted() throws Exception {

    final Panel panel = new Panel(RandomGenerator.randomPanelSerial(),
	RandomGenerator.randomLongitude(),
	RandomGenerator.randomLatitude(),
	"Solar inc.");

    mvc.perform(post("/api/register")
	.content("{\"serial\":\"" + panel.getSerial() + "\",\"longitude\":" + panel.getLongitude() + ",\"latitude\":"
	    + panel.getLatitude() + ",\"brand\":\"" + panel.getBrand() + "\"}")
	.contentType(MediaType.APPLICATION_JSON_UTF8))
	.andExpect(status().isAccepted())
	.andDo(print());

    verify(panelService, times(1)).register(panel);
  }

  @Test
  public void givenRegisteredPanel_whenRegisterPanel_thenReturnConflict() throws Exception {

    final Panel panel = new Panel(RandomGenerator.generatedId(),
	RandomGenerator.randomPanelSerial(),
	RandomGenerator.randomLongitude(),
	RandomGenerator.randomLatitude(),
	"Solar inc.");

    when(panelService.findBySerial(panel.getSerial())).thenReturn(panel);

    mvc.perform(post("/api/register")
	.content("{\"serial\":\"" + panel.getSerial() + "\",\"longitude\":"
	    + panel.getLongitude() + ",\"latitude\":" + panel.getLatitude() + ",\"brand\":\"" + panel.getBrand()
	    + "\"}")
	.contentType(MediaType.APPLICATION_JSON_UTF8))
	.andExpect(status().isConflict())
	.andDo(print());

    verify(panelService, never()).register(panel);
  }

  @Test
  public void givenRegisteredPanel_whenGetPanelInfo_thenReturnJsonStructure() throws Exception {

    final Panel panel = new Panel(RandomGenerator.randomPanelSerial(),
	RandomGenerator.randomLongitude(),
	RandomGenerator.randomLatitude(),
	"Solar inc.");

    when(panelService.findBySerial(panel.getSerial())).thenReturn(panel);

    mvc.perform(get("/api/panels/" + panel.getSerial() + "/info"))
	.andExpect(status().isOk())
	.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
	.andExpect(jsonPath("$.serial", is(panel.getSerial())))
	.andExpect(jsonPath("$.brand", is(panel.getBrand())))
	.andExpect(jsonPath("$.longitude", equalTo(panel.getLongitude().doubleValue())))
	.andExpect(jsonPath("$.latitude", equalTo(panel.getLatitude().doubleValue())))
	.andDo(print());
  }

  @Test
  public void givenWrongSerial_whenGetPanelInfo_thenReturnNotFound() throws Exception {
    mvc.perform(get("/api/panels/ABC/info"))
	.andExpect(status().isNotFound())
	.andDo(print());
  }

}
