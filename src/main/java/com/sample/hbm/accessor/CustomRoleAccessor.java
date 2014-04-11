package com.sample.hbm.accessor;

import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.PropertyNotFoundException;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.property.Getter;
import org.hibernate.property.PropertyAccessor;
import org.hibernate.property.Setter;

import com.sample.model.Employee;
import com.sample.model.EmployeeTypeEnum;

public class CustomRoleAccessor implements PropertyAccessor {

	public CustomRoleAccessor() {
		// TODO Auto-generated constructor stub
	}

	public Getter getGetter(Class theClass, String propertyName)
			throws PropertyNotFoundException {
		return new RoleGetter();
	}

	public Setter getSetter(Class theClass, String propertyName)
			throws PropertyNotFoundException {
		return new RoleSetter();
	}

	private static class RoleGetter implements Getter {

		private static String methodName = null;
		private static Method method = null;

		private boolean insert = false;

		static {
			try {
				Class[] cArg = new Class[1];
				cArg[0] = Long.class;
				method = RoleGetter.class.getMethod("getRole", cArg);
				methodName = method.getName();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		public RoleGetter() {
			// TODO Auto-generated constructor stub
		}

		public Object get(Object owner) throws HibernateException {
			if (insert) {
				insert = false; //reset insert
				return ((Employee) owner).getRole();
			} else {
				return getRole(new Long(1));
			}
		}

		public Object getForInsert(Object owner, Map mergeMap,
				SessionImplementor session) throws HibernateException {
			Employee employee = (Employee) owner;
			insert = true;
			return employee.getRole();
		}

		public Member getMember() {
			return null;
		}

		public Class getReturnType() {
			return method.getReturnType();
		}

		public String getMethodName() {
			return methodName;
		}

		public Method getMethod() {
			return method;
		}

		public EmployeeTypeEnum getRole(Long id) {
			return EmployeeTypeEnum.MANAGER;
		}
		
	}

	private static class RoleSetter implements Setter {

		private static String methodName = null;
		private static Method method = null;

		static {
			try {
				Class[] cArg = new Class[3];
				cArg[0] = Object.class;
				cArg[1] = Object.class;
				cArg[2] = SessionFactoryImplementor.class;
				method = RoleSetter.class.getMethod("set", cArg);
				methodName = method.getName();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		public RoleSetter() {

		}

		public void set(Object target, Object value,
				SessionFactoryImplementor factory) throws HibernateException {
			Employee employee = (Employee) target;
			EmployeeTypeEnum val = (EmployeeTypeEnum) value;
			employee.setRole(val);
		}

		public String getMethodName() {
			return methodName;
		}

		public Method getMethod() {
			return method;
		}

	}
}
