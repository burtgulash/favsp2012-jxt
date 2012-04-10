<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="2.0" 
		xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
		xmlns:func="funkce">

<!-- Funkce pro vytvoření rozvrhu pro daný semester. -->
<xsl:function name="func:vytvoritRozvrh">
	<xsl:param name="semestr" />

	<table>
		<tr>
		<xsl:for-each select="1 to 14">
			<th><xsl:value-of select="." /></th>
		</xsl:for-each>		
		</tr>
		<tr>
		</tr>
	</table>
</xsl:function>


<!-- Match root. -->
<xsl:template match="/">
<html>
	<head>
	</head>
		<xsl:value-of select="func:vytvoritRozvrh('ZS')" />
		<hr />
		<xsl:value-of select="func:vytvoritRozvrh('LS')" />
	<body>
	</body>
</html>
</xsl:template>
</xsl:stylesheet>
