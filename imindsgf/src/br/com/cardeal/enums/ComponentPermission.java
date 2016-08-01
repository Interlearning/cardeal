package br.com.cardeal.enums;

import java.util.ArrayList;
import java.util.List;

public enum ComponentPermission {
	LOTE 				
	(1, 2, "EMBALAGEM - ALTERAR LOTE")
	,CADASTRO_PRODUTOS	
	(2, 1, "CADASTROS - PRODUTO")
	,CADASTRO_UNIDADE	
	(3, 1, "CADASTROS - UNIDADE DE MEDIDA")
	,CADASTRO_CLIENTE_FORNECEDOR	
	(4, 1, "CADASTROS - CLIENTE/FORNECEDORES")
	,CADASTRO_FILIAIS	
	(5, 1, "CADASTROS - FILIAIS")
	,CADASTRO_USUARIOS	
	(6, 1, "CADASTROS - USUÁRIOS")
	,CADASTRO_PERFIL	
	(7, 1, "CADASTROS - PERFIS")
	,CADASTRO_EMPRESAS
	(8, 1, "CADASTROS - EMPRESAS")
	,RELATORIO_ESTOQUE_ATUAL	
	(9, 1, "RELATORIOS - ESTOQUE ATUAL")
	,RELATORIO_PRODUCAO_DIARIA	
	(10, 1, "RELATORIOS - PRODUÇÃO DIÁRIA")
	,RELATORIO_PRODUCAO_PALLETS	
	(11, 1, "RELATORIOS - PALLETS")
	,RELATORIO_PRODUCAO_HISTORICO	
	(12, 1, "RELATORIOS - HISTÓRICO")
	,MANUTENCAO_CONFIGURACOES	
	(13, 1, "MANUTENÇÃO - CONFIGURAÇÕES")
	,MANUTENCAO_TERIMNAIS	
	(14, 1, "MANUTENÇÃO - TERMINAIS")
	,MANUTENCAO_ESTOQUE	
	(15, 1, "MANUTENÇÃO - AJUSTE DE ESTOQUE")
	,MANUTENCAO_PALLET
	(16, 1, "MANUTENÇÃO - PALLET")
	,MANUTENCAO_MONITOR	
	(17, 1, "MANUTENÇÃO - MONITOR")
	,MANUTENCAO_ETIQUETAS	
	(18, 1, "MANUTENÇÃO - ETIQUETAS")
	,MANUTENCAO_EXPEDIÇÃO	
	(19, 1, "MANUTENÇÃO - EXPEDIÇÃO")	
	,MENU_OPERACOES_MENU_EMBALAGEM
	(20, 2, "MENU DE OPERAÇÕES - MENU DE EMBALAGEM")
	,MENU_OPERACOES_EXPEDICAO
	(21, 2, "MENU DE OPERAÇÕES - EXPEDIÇÃO")
	,MENU_OPERACOES_RECEBIMENTO
	(22, 2, "MENU DE OPERAÇÕES - RECEBIMENTO")
	,MENU_OPERACOES_ABRE_CAIXA
	(23, 2, "MENU DE OPERAÇÕES - ABRE CAIXA")
	,MENU_OPERACOES_RELATORIOS
	(24, 2, "MENU DE OPERAÇÕES - RELATÓRIOS")
	,MENU_EMBALAGEM_EMBALAGEM
	(25, 2, "MENU DE EMBALAGEM - EMBALAGEM")
	,MENU_EMBALAGEM_REPALETIZAR
	(26, 2, "MENU DE EMBALAGEM - REPALETIZAR")
	,MENU_EMBALAGEM_PALLET_VIRTUAL
	(27, 2, "MENU DE EMBALAGEM - PALETE VIRUTAL")
	,MENU_EMBALAGEM_ESTOQUE_TRANSITO
	(28, 2, "MENU DE EMBALAGEM - ESTOQUE EM TRÂNSITO")
	,MENU_EMBALAGEM_ESTOQUE_INSUMO
	(29, 2, "MENU DE EMBALAGEM - ESTOQUE INSUMOS")
	,MENU_EMBALAGEM_EXCECAO_PESO_PADRAO
	(30, 2, "MENU DE EMBALAGEM - EXCEÇÃO PESO PADRÃO")
	,EXPEDICAO_COMPLEMENTAR_ITENS	
	(31, 2, "EXPEDIÇÃO - COMPLEMENTO DE ITEM")
	,EXPEDICAO_QTD_MAIOR_SOLICITADO		
	(32, 2, "EXPEDIÇÃO - QUANTIDADE MAIOR QUE SOLICITADO")
	,EXPEDICAO_MATERIA_PRIMA	
	(33, 2, "EXPEDIÇÃO - MATÉRIA PRIMA")
	,MENU_RECEBIMENTO_COMPRAS		
	(34, 2, "RECEBIMENTO - MENU DE COMPRAS")
	,MENU_RECEBIMENTO_DEVOLUCAO
	(35, 2, "RECEBIMENTO - MENU DE DEVOLUÇÃO") 
	,MANUTENCAO_RECEBIMENTO
	(36, 1, "MANUTENÇÃO - RECEBIMENTO")
	,MANUTENCAO_ESTOQUE_NAO_PALETIZADO
	(37, 1, "MANUTENÇÃO - ESTOQUE NÃO PALETIZADO")
	,MENU_OPERACOES_RELATORIOS_PRODUCAO_DIARIA
	(38, 2, "MENU DE OPERAÇÕES - RELATÓRIOS - PRODUÇÃO DIARIA")
	,MENU_OPERACOES_RELATORIOS_PALETE
	(39, 2, "MENU DE OPERAÇÕES - RELATÓRIOS - PALETE"),
	;
	
	private final int id;
	private final int grupo; // 1=Servidor / 2=Estação
	private final String name;
	
	ComponentPermission(int id, int grupo, String name) { 
		this.id = id; 
		this.grupo = grupo;
		this.name = name;
		
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	public static String getById(String idRole) 
	{
		ComponentPermission[] roles = ComponentPermission.values();		
		
		for ( ComponentPermission permission : roles ){
			
			if ( String.valueOf( permission.getId() ).equals( idRole ) ) return permission.getRoleFormattedToDeniedRoles();
			
		}
		
		return null;
	}
	
	public static List<ComponentPermission> getComponentPermissionServer() 
	{
		ComponentPermission[] roles = ComponentPermission.values();
		List<ComponentPermission> components = new ArrayList<ComponentPermission>();
		for ( ComponentPermission permission : roles )
		{
			if ( permission.getGrupo() == 1 )
			{
				components.add(permission);
			}
		}
		return components;
	}
	
	public static List<ComponentPermission> getComponentPermissionStation() 
	{
		ComponentPermission[] roles = ComponentPermission.values();
		List<ComponentPermission> components = new ArrayList<ComponentPermission>();
		for ( ComponentPermission permission : roles )
		{
			if ( permission.getGrupo() == 2 )
			{
				components.add(permission);
			}
		}
		return components;
	}
	
	public String getRoleFormattedToDeniedRoles()
	{
		return String.valueOf( "[" + this.getId() ) + "] " + this.getName();
	}

	public int getGrupo() {
		return grupo;
	}

	public static List<ComponentPermission> getAllComponents() 
	{
		List<ComponentPermission> components = new ArrayList<ComponentPermission>();
		components.addAll( getComponentPermissionServer() );
		components.addAll( getComponentPermissionStation() );
		return components;
	}

}
