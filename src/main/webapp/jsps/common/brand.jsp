<%@ page contentType="text/html;charset=gbk"%>
<header id="brand" data-bind="with : user">
	<div class="row">
		<div class="appname hide-on-phones">ʲôʲôһ��ϵͳ</div>
		<address>
			<span>
				<div class="row">
					<b data-bind="text : name"></b> &nbsp; <a title="��ȫ�˳�ϵͳ" href="/crud/logout"><i class="icon-power-off small"></i></a> &nbsp;
					<a title="�޸�����" href="#" data-bind="click : $root.openResetMyPasswordDialog"><i class="icon-cog small"></i></a>
				</div>
			</span>
		</address>
		<br>
		<br>
	</div>
	<div id="resetPasswordDialogInBrandJsp" style="display: none;">
			<input type="password"  placeholder="������ɵ�����" required data-bind="value : $root.oldPassword"> 
			<input type="password"  placeholder="�������µ�����" required data-bind="value : $root.newPassword"> 
	</div>
</header>
<script>
	$(document).ready(function() {

		var UserModel = function() {
			var self = this;
			self.user = ko.observable(new User());
			self.oldPassword = ko.observable('');
			self.newPassword = ko.observable('');
			self.loadMe = function() {
				$.ajax({
					url : '/crud/loadMe.ls',
					success : function(data) {
						
						if (data && data.id) {
							self.user(data);
						}
					}
				});
			};
			self.loadMe();
			
			self.openResetMyPasswordDialog = function() {
				
				$('#resetPasswordDialogInBrandJsp').dialog({
					modal : true,
					width : 'auto',
					height : 'auto',
					open : function(e) {
						changeButtonStyleForPopup(e);
					},
					
					buttons : {
						'�ύ' : function() {
							
							$.ajax({
								url : '/crud/resetMyPassword.ls',
								method : 'POST',
								data : {
									oldPassword : self.oldPassword(),
									newPassword : self.newPassword()
								},
								success : function(data) {
									if (isOK(data)) {
										
										success(data.message);
										
										closeDialog('resetPasswordDialogInBrandJsp');
									} else {
										
										fail(data.message);
									}
								}
							});
						},
						'�رմ���' : function() {
							closeDialog('resetPasswordDialogInBrandJsp');
						}
					}
					
				});
			};
			
		};
		var userModel = new UserModel();
		var $userContainer = $('#brand')[0];
		ko.applyBindings(userModel, $userContainer);
	});
</script>