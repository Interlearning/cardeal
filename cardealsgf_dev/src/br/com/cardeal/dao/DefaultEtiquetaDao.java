
package br.com.cardeal.dao;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import br.com.cardeal.enums.ModeloEtiqueta;
import br.com.cardeal.filter.EtiquetaFilter;
import br.com.cardeal.model.Etiqueta;
import br.com.cardeal.services.EtiquetaService;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class DefaultEtiquetaDao implements EtiquetaDao {

	private final Session session;

	public DefaultEtiquetaDao(Session session) {
		this.session = session;
	}

	public Etiqueta find(int id) {		
		return (Etiqueta) session.createCriteria(Etiqueta.class)
			    .add( Restrictions.eq("id", id) )
			    .uniqueResult();
	}

	public void add(Etiqueta etiqueta) {		
		session.save(etiqueta);
	}

	public void refresh(Etiqueta etiqueta) {
		session.refresh(etiqueta);
	}

	public void update(Etiqueta etiqueta) {
		session.merge(etiqueta);
	}

	public String delete(Etiqueta etiqueta) 
	{
		try 
		{
			etiqueta = find(etiqueta.getId());
			if (etiqueta != null) 
			{
				session.delete(etiqueta);
				session.flush();
			}
		} 
		catch (Exception e) 
		{
			return e.getCause().getMessage();
		} 
		finally 
		{
			session.clear();
		}
		
		return "OK";

	}

	@SuppressWarnings("unchecked")
	public List<Etiqueta> list() {
		List<Etiqueta> list =  (List<Etiqueta>)(session.createCriteria(Etiqueta.class)).list();
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<Etiqueta> list( EtiquetaFilter filter ) {
		
	
		List<Etiqueta> list; // =  (List<Etiqueta>)(session.createCriteria(Etiqueta.class)).list();
		
		Criteria c = session.createCriteria(Etiqueta.class);		
		
		if(filter != null) {
			
			if ( filter.getModeloEtiqueta() != null )
				c.add(Restrictions.eq("modeloEtiqueta", filter.getModeloEtiqueta()));
			
		}
		
		list =  (List<Etiqueta>)c.list();
		
		return list;
	}

	@Override
	public void initCadLabels() {

		EtiquetaService etiquetaService = new EtiquetaService();
		List<Etiqueta> etiquetas = new ArrayList<Etiqueta>();
		
		etiquetas.add( new Etiqueta("etiquetacaixa.txt", false, ModeloEtiqueta.CAIXA, false) );
		etiquetas.add( new Etiqueta("etiquetacaixa-especifica.txt", false, ModeloEtiqueta.CAIXA, true) );
		etiquetas.add( new Etiqueta("pallet.txt", false, ModeloEtiqueta.PALLET, false) );
		etiquetas.add( new Etiqueta("etiquetacaixapaodeacucar.txt", true, ModeloEtiqueta.CAIXA, true) );
		etiquetas.add( new Etiqueta("etiquetapalletpaodeacucar.txt", true, ModeloEtiqueta.PALLET, true) );
		etiquetas.add( new Etiqueta("etiquetapalletpaodeacucar.txt", true, ModeloEtiqueta.PALLET, true) );
		etiquetas.add( new Etiqueta("etiquetapalletvirtual.txt", true, ModeloEtiqueta.PALLET, false) );
		
		for ( Etiqueta etiqueta : etiquetas ){
			etiquetaService.adiciona(etiqueta);
		}
		
	}

	@Override
	public Etiqueta findByNameArq(String etiquetaPallet) {
		return (Etiqueta) session.createCriteria(Etiqueta.class)
			    .add( Restrictions.eq("nameEtq", etiquetaPallet) )
			    .uniqueResult();
	}
	
}
