package com.zsm.wordrember.operator;

import com.zsm.wordrember.data.Book;

public interface BookKeeper {

	void setBook( Book book );
	Book getBook();
}
