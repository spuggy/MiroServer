<h1><fmt:message key="menu.admin"/></h1>
<menu:useMenuDisplayer name="Velocity" config="WEB-INF/classes/cssHorizontalMenu.vm" permissions="rolesAdapter">
<ul id="sidebar-nav" class="menuList">   

    
     
     <menu:displayMenu name="ViewUsers" />
     <menu:displayMenu name="SettingMenu" />
     <menu:displayMenu name="ActiveUsers" />
     <menu:displayMenu name="ReloadContext" />
     <menu:displayMenu name="FlushCache" />
      <menu:displayMenu name="WebPageMenu" />
     <menu:displayMenu name="All Responses"/>
     <menu:displayMenu name="My Responses"/>
      <menu:displayMenu name="New Survey"/>
     <menu:displayMenu name="All Surveys"/>
     <menu:displayMenu name="Import Survey"/>
     <menu:displayMenu name="NewLookupDef"/>     
     <menu:displayMenu name="LookupDefs"/>
      <menu:displayMenu name="MobrizAlertMenu"/>
        
     
</ul>

</menu:useMenuDisplayer>
