package ru.alkise.trader.model.store;

import ru.alkise.trader.model.ClientIntf;
import ru.alkise.trader.model.ClientType;
import ru.alkise.trader.model.DocumentType;
import ru.alkise.trader.model.ManagerIntf;
import ru.alkise.trader.model.OrderIntf;
import ru.alkise.trader.model.OrganizationIntf;
import ru.alkise.trader.model.factory.ClientFactory;
import ru.alkise.trader.model.factory.ManagerFactory;
import ru.alkise.trader.model.factory.OrderFactory;
import ru.alkise.trader.model.factory.OrganizationFactory;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferencesDataSaver implements DataSaverIntf {
	private SharedPreferences sp;
	
	public SharedPreferencesDataSaver(String dbName, Context context) {
		sp = context.getSharedPreferences(dbName, Context.MODE_PRIVATE);
	}
	
	@Override
	public void store(OrderIntf order) {
		Editor editor = sp.edit(); 
		DocumentType documentType = order.getOrderDocumentType();
		OrganizationIntf organization = order.getOrderOrganization();
		ManagerIntf manager = order.getOrderManager();
		ClientIntf client = order.getOrderClient();
		
		if (documentType != null) {
			editor.putInt(DocumentType.DOCUMENT_TYPE_CODE, documentType.getDocumentTypeCode());
		}
		
		if (organization != null) {
			editor.putInt(OrganizationIntf.ORGANIZATION_CODE, organization.getOrganizationCode());
			editor.putString(OrganizationIntf.ORGANIZATION_NAME, organization.getOrganizationName());
		}
		
		if (manager != null) {
			editor.putInt(ManagerIntf.MANAGER_CODE, manager.getManagerCode());
			editor.putString(ManagerIntf.MANAGER_NAME, manager.getManagerName());
		}
		
		if (client != null) {
			editor.putInt(ClientIntf.CLIENT_CODE, client.getClientCode());
			if (client.getClientType() != null) {
				editor.putString(ClientType.CLIENT_TYPE_CODE, client.getClientType().getClientTypeCode());
			}
			editor.putString(ClientIntf.CLIENT_SHORT_NAME, client.getClientShortName());
			editor.putString(ClientIntf.CLIENT_FULL_NAME, client.getClientFullName());
		}
		editor.commit();
	}
	
	@Override
	public OrderIntf load() {
		OrderIntf order = OrderFactory.createOrder();
		DocumentType documentType = DocumentType.getTypeByCode(sp.getInt(DocumentType.DOCUMENT_TYPE_CODE, 1));
		order.setOrderDocumentType(documentType);
		
		int organizationCode = sp.getInt(OrganizationIntf.ORGANIZATION_CODE, -1);
		String organizationName = sp.getString(OrganizationIntf.ORGANIZATION_NAME, null);
		if (organizationName != null ) {
			order.setOrderOrganization(OrganizationFactory.createOrganization(organizationCode, organizationName));
		}
		
		int managerCode = sp.getInt(ManagerIntf.MANAGER_CODE, -1);
		String managerName = sp.getString(ManagerIntf.MANAGER_NAME, null);
		if (managerName != null) {
			order.setOrderManager(ManagerFactory.createManager(managerCode, managerName));
		}
		
		int clientCode = sp.getInt(ClientIntf.CLIENT_CODE, -1);
		String clientShortName = sp.getString(ClientIntf.CLIENT_SHORT_NAME, null);
		String clientFullName = sp.getString(ClientIntf.CLIENT_FULL_NAME, null);
		String clientTypeCode = sp.getString(ClientType.CLIENT_TYPE_CODE, null);
		if ((clientShortName != null) && (clientFullName != null)) {
			ClientIntf client = ClientFactory.createClient(clientCode, clientShortName, clientFullName);
			if (clientTypeCode != null) {
				client.setClientType(ClientType.getClientTypeById(clientTypeCode));
			}
			order.setOrderClient(client);
		}
		
		return order;
	}
}
