$classToMockNames, classToMockInstanceNames: {classToMockName, classToMockInstanceName | final $classToMockName$ $classToMockInstanceName$ = mock($classToMockName$.class, "Mock$classToMockName$_" + $params: {$it.name$}; separator=" + \"_\" + "$);}; separator="\n"$
checking(new Expectations() {{
	$jmockInvokeSequences: {$it$;}; separator="\n"$
}});
