<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="2.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:fn="http://www.w3.org/2005/xpath-functions">
	
<xsl:variable name="this" select="/" />
<xsl:variable name="data" select="document('data.xml')" />


<xsl:template name="zobrazitPredmety">
	<xsl:param name="predmety" />
	<xsl:for-each select="$predmety/predmet">

		<xsl:variable name="mezeraCasu" select="
		xs:time(zacatek) - xs:time(preceding-sibling::predmet[1]/konec) - xs:dayTimeDuration('PT10M')" />
		<xsl:variable name="mezeraHodin" 
				select="(fn:hours-from-duration($mezeraCasu) * 60 
					  + fn:minutes-from-duration($mezeraCasu)) 
					  div 55" />
		<!-- Kladná mezera -> vytvořit buňku s mezerou. -->
		<xsl:if test="$mezeraHodin > 1">
			<td>
				<xsl:attribute name="colspan">
					<xsl:value-of select="$mezeraHodin" />
				</xsl:attribute>
			</td>
		</xsl:if>

		<!-- Záporná mezera znamená kolizi. -->
		<xsl:if test="$mezeraHodin &lt; 0">
			<!-- Zajebatý xslt, seru na to kurva. -->
			</tr><tr>
		</xsl:if>

		<xsl:variable name="rozdilCasu" select="
					xs:time(konec) - xs:time(zacatek) + xs:time('00:10:00')" />
		<xsl:variable name="colspan" 
				select="(fn:hours-from-time($rozdilCasu) * 60 
					  + fn:minutes-from-time($rozdilCasu)) 
					  div 55" />

		<td>
			<xsl:if test="$colspan > 1">
				<xsl:attribute name="colspan">
					<xsl:value-of select="$colspan" />
				</xsl:attribute>
			</xsl:if>
			<xsl:choose>
				<xsl:when test="typ = 'Př'">
					<xsl:attribute name="class">prednaska</xsl:attribute>
				</xsl:when>
				<xsl:when test="typ = 'Cv'">
					<xsl:attribute name="class">cviceni</xsl:attribute>
				</xsl:when>
				<xsl:when test="typ = 'Se'">
					<xsl:attribute name="class">seminar</xsl:attribute>
				</xsl:when>
				<xsl:otherwise>
					<xsl:attribute name="class">prazdne</xsl:attribute>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:value-of select="nazev" />
		</td>
	</xsl:for-each>
</xsl:template>

<xsl:template match="den">
	<xsl:param name="semestr" />
	<xsl:variable name="den" select="." />

	<tr>
		<th><xsl:value-of select="." /></th>
		<xsl:variable name="serazenePredmety">
			<xsl:for-each select="$this/student/predmet[den = $den
												and semestr = $semestr]">
				<xsl:sort select="zacatek" />
				<xsl:copy-of select="." />
			</xsl:for-each>
		</xsl:variable>

		<xsl:call-template name="zobrazitPredmety">
			<xsl:with-param name="predmety" select="$serazenePredmety" />
		</xsl:call-template>
	</tr>
</xsl:template>


<!-- Funkce pro vytvoření rozvrhu pro daný semester. -->
<xsl:template name="vytvoritRozvrh">
	<xsl:param name="semestr" />

	<table class="tabulka">
		<tr>
			<th><!-- První buňka prázdná. --></th>
			<xsl:for-each select="$data/data/casy/hodina">
				<th><xsl:value-of select="@cislo" /></th>
			</xsl:for-each>		
		</tr>

		<!-- Vytvořit řádek pro každý den. -->
		<xsl:apply-templates select="$data/data/dny">
			<xsl:with-param name="semestr" select="$semestr" />
		</xsl:apply-templates>
	</table>
</xsl:template>


<!-- Match root. -->
<xsl:template match="/">
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="style.css" />
	</head>
	<body>
		<h1>Rozvrh pro <xsl:value-of select="/student/osobniCislo" /></h1>
		<xsl:call-template name="vytvoritRozvrh">
			<xsl:with-param name="semestr" select="'ZS'" />
		</xsl:call-template>
		<hr />
		<xsl:call-template name="vytvoritRozvrh">
			<xsl:with-param name="semestr" select="'LS'" />
		</xsl:call-template>
	</body>
</html>
</xsl:template>
</xsl:stylesheet>
