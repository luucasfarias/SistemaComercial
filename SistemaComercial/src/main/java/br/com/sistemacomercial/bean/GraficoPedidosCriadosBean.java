package br.com.sistemacomercial.bean;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;

import br.com.sistemacomercial.model.Usuario;
import br.com.sistemacomercial.repository.Pedidos;

@Named
@RequestScoped
public class GraficoPedidosCriadosBean {

	private static DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM");

	@Inject
	private Pedidos pedidos;
	

	// private UsuarioSistema usuarioLogado;

	private LineChartModel model;

	public void preRender() {
		this.model = new LineChartModel();
		this.model.setTitle("Avaliação de vendas realizadas pelo sistema");
		this.model.setLegendPosition("ne");
		this.model.setAnimate(true);

		this.model.getAxes().put(AxisType.X, new CategoryAxis());

		adicionarSerie("Todos os pedidos", null);
		adicionarSerie("Meus pedidos", null);
	}

	private void adicionarSerie(String rotulo, Usuario criadoPor) {
		Map<Date, BigDecimal> valoresPorData = this.pedidos.valoresTotaisPorData(15, criadoPor);

		ChartSeries series = new ChartSeries(rotulo);

		for (Date data : valoresPorData.keySet()) {
			series.set(DATE_FORMAT.format(data), valoresPorData.get(data));
		}

		this.model.addSeries(series);
	}

	public LineChartModel getModel() {
		return model;
	}

}