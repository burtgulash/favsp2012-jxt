<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="2.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:fn="http://www.w3.org/2005/xpath-functions">
	<xsl:output method="html" indent="yes" />
	
<xsl:variable name="this" select="/" />
<xsl:variable name="data" select="document('data.xml')" />


<xsl:template match="den">
	<xsl:param name="semestr" />
	<xsl:variable name="den" select="." />

	<tr>
		<th><xsl:value-of select="." /></th>
		<xsl:for-each select="$data/data/casy/hodina">
			<xsl:variable name="hodina" select="." />
	<!-- Provést natural join políčka v tabulce a předmětů studenta. -->
			<xsl:variable name="predmet" 
						select="$this/student/predmet
						[semestr    = $semestr 
						and den     = $den 
						and xs:time(zacatek) = xs:time($hodina/zacatek)][1]" />

			<xsl:if test="0 = count($this/student/predmet
							[semestr    = $semestr 
							and den     = $den 
							and xs:time(zacatek) &lt; xs:time($hodina/zacatek)
							and xs:time(konec) &gt;= xs:time($hodina/konec)])">
				<!-- 
					 Vypočítat počet hodin předmětu (N) z rovnice:
					 konec - start = N * 45 + (N - 1) * 10 
				  -->
				<xsl:variable name="rozdilCasu" select="xs:time($predmet/konec)
						   - xs:time($predmet/zacatek) + xs:time('00:10:00')" />
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
						<xsl:when test="$predmet/typ = 'Př'">
							<xsl:attribute name="class">prednaska</xsl:attribute>
						</xsl:when>
						<xsl:when test="$predmet/typ = 'Cv'">
							<xsl:attribute name="class">cviceni</xsl:attribute>
						</xsl:when>
						<xsl:when test="$predmet/typ = 'Se'">
							<xsl:attribute name="class">seminar</xsl:attribute>
						</xsl:when>
						<xsl:otherwise>
							<xsl:attribute name="class">prazdne</xsl:attribute>
						</xsl:otherwise>
					</xsl:choose>
					<xsl:value-of select="$predmet/nazev" />
				</td>
			</xsl:if>
		</xsl:for-each>
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
