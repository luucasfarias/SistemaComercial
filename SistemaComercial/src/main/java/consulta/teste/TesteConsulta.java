package consulta.teste;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;

import br.com.sistemacomercial.model.Pedido;
import br.com.sistemacomercial.model.Usuario;
import br.com.sistemacomercial.model.VO.DataValor;

public class TesteConsulta {

	public static void main(String[] args) {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("pedido");
		EntityManager manager = factory.createEntityManager();
		Session session = manager.unwrap(Session.class);
		
		Usuario usuario = new Usuario();
		usuario.setId(1L);

		Map<Date, BigDecimal> valores = valoresTotaisPorData(15, usuario, session);

		for (Date data : valores.keySet()) {
			System.out.println(data + " = " + valores.get(data));
		}

		manager.close();
	}

	@SuppressWarnings({ "unchecked" })
	public static Map<Date, BigDecimal> valoresTotaisPorData(Integer numeroDeDias, Usuario criadoPor, Session session) {
		Calendar dataInicial = Calendar.getInstance();

		numeroDeDias -= 1;

		dataInicial = DateUtils.truncate(dataInicial, Calendar.DAY_OF_MONTH);
		dataInicial.add(Calendar.DAY_OF_MONTH, numeroDeDias * -1);

		Map<Date, BigDecimal> resultado = criarMapaVazio(numeroDeDias, dataInicial);

		// select date(data_criacao) as data, sum (valor_total) as valor
		// from pedido where data_criacao >= :dataInicial nd vendedor_id =
		// :criado_por group by date(data_criacao)

		Criteria criteria = session.createCriteria(Pedido.class);
		criteria.setProjection(Projections.projectionList()
				.add(Projections.sqlGroupProjection("date(data_criacao) as data", "date(data_criacao)",
						new String[] { "data" }, new Type[] { StandardBasicTypes.DATE }))
				.add(Projections.sum("valorTotal").as("valor")))
				.add(Restrictions.ge("dataCriacao", dataInicial.getTime())); // projeção
																				// SUM

		if (criadoPor != null) {
			criteria.add(Restrictions.eq("vendedor", criadoPor));
		}

		List<DataValor> valoresPorData = criteria.setResultTransformer(Transformers.aliasToBean(DataValor.class))
				.list();

		for (DataValor dataValor : valoresPorData) {
			resultado.put(dataValor.getData(), dataValor.getValor());
		}

		return resultado;
	}

	private static Map<Date, BigDecimal> criarMapaVazio(Integer numeroDeDias, Calendar dataInicial) {

		dataInicial = (Calendar) dataInicial.clone();
		Map<Date, BigDecimal> mapaInicial = new TreeMap<>();

		for (int i = 0; i <= numeroDeDias; i++) {
			mapaInicial.put(dataInicial.getTime(), BigDecimal.ZERO);
			dataInicial.add(Calendar.DAY_OF_MONTH, 1);
		}

		return mapaInicial;
	}

}