package kr.ac.kw.Commons;

public enum GlobalEnum {
	Ballad{
		@Override
		public String getString() {
			return "Ballad";
		}
	},
	Dance{
		@Override
		public String getString() {
			return "Dance";
		}
	},
	Soul{
		@Override
		public String getString() {
			return "Soul";
		}
	},
	Rock{
		@Override
		public String getString() {
			return "Rock";
		}
	},
	Rap{
		@Override
		public String getString() {
			return "Rap%";
		}
	},
	Default{
		@Override
		public String getString() {
			// TODO Auto-generated method stub
			return null;
		}
		
	};
	public abstract String getString();
	

	}
	
	
