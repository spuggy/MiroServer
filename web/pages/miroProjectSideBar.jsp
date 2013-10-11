<h1>Menu</h1>
<menu:useMenuDisplayer name="Velocity" config="WEB-INF/classes/cssHorizontalMenu.vm" permissions="rolesAdapter">
<ul id="sidebar-nav" class="menuList">   
    
     <menu:displayMenu name="MiroProjects"/>
      <menu:displayMenu name="miroteampie"/>
      <menu:displayMenu name="miroteamreport"/>
     <menu:displayMenu name="myprofile"/>
     <menu:displayMenu name="help"/>
     <menu:displayMenu name="myaccount"/>
    <menu:displayMenu name="departmentadmin"/>
  	
  	
</ul>

</menu:useMenuDisplayer>
<div id="shoppingCartUI" style="display:none">
<h1>Shopping Cart</h1>
<dl class="profile">
<dd>
<table id="shoppingCartTable">
<tbody id="shoppingCartTableBody">
</tbody>
</table>
</dd>
<dt>&nbsp;</dt>
</dl>
</div>

<h1>Profile</h1>
 <dl class="profile">
 <dd>Name:</dd><dt> <authz:authentication operation="fullName"/></dt>
 <dd>Tel: </dd><dt><authz:authentication operation="phoneNumber"/></dt>
 <dd>Email:</dd><dt> <authz:authentication operation="email"/></dt>
 
 </dl>