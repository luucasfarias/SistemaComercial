package br.com.sistemacomercial.bean;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;

@ManagedBean(name = "GraficoLinhaMB")
public class GraficoArea implements Serializable {

	private static final long serialVersionUID = 1L;
	private LineChartModel lineModel1;

	@PostConstruct
	public void init() {
		createLineModels();
	}

	public CartesianChartModel getLineModel1() {
		return lineModel1;
	}

	private void createLineModels() {
		lineModel1 = initLinearModel();
		lineModel1.setTitle("Avaliação de Vendas");
		lineModel1.setLegendPosition("e");
		lineModel1.setShowPointLabels(true);
		lineModel1.setAnimate(true);
		lineModel1.getAxes().put(AxisType.X, new CategoryAxis("Pesquisas"));
		Axis yAxis = lineModel1.getAxis(AxisType.Y);
		yAxis.setLabel("% de vendas");
		yAxis.setMin(0);
		yAxis.setMax(100);

	}

	private LineChartModel initLinearModel() {
		LineChartModel model = new LineChartModel();

		ChartSeries series1 = new ChartSeries();
		series1.setLabel("Vendedor 1");

		series1.set("Pesquisa 1", 80);
		series1.set("Pesquisa 2", 42);
		series1.set("Pesquisa 3", 25);
		series1.set("Pesquisa 4", 44);
		series1.set("Pesquisa 5", 80);

		ChartSeries series2 = new ChartSeries();
		series2.setLabel("Vendedor 2");

		series2.set("Pesquisa 1", 39);
		series2.set("Pesquisa 2", 22);
		series2.set("Pesquisa 3", 45);
		series2.set("Pesquisa 4", 25);
		series2.set("Pesquisa 5", 29);

		ChartSeries series3 = new ChartSeries();
		series3.setLabel("Vendedor 3");

		series3.set("Pesquisa 1", 10);
		series3.set("Pesquisa 2", 12);
		series3.set("Pesquisa 3", 16);
		series3.set("Pesquisa 4", 15);
		series3.set("Pesquisa 5", 19);

		model.addSeries(series1);
		model.addSeries(series2);
		model.addSeries(series3);

		return model;
	}

}