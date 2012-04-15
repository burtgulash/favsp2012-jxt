<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="2.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:fn="http://www.w3.org/2005/xpath-functions">
<xsl:output method="xml" encoding="utf-8" indent="yes" />
	
<xsl:variable name="this" select="/" />
<xsl:variable name="data" select="document('data.xml')" />

<xsl:template name="zaSemestr">
	<xsl:param name="semestr" />
	<semestr>
		<zimniLetni><xsl:value-of select="$semestr" /></zimniLetni>
		<xsl:for-each select="$data/data/dny/den">
			<xsl:variable name="den" select="." />
			<radka>
				<den><xsl:value-of select="." /></den>
				<xsl:for-each select="$this/student/predmet[semestr = $semestr
													and den = $den]">
					<xsl:sort select="zacatek" />
					<xsl:copy-of select="." />
				</xsl:for-each>
			</radka>
		</xsl:for-each>
	</semestr>
</xsl:template>


<xsl:template match="/" mode="faze1">
	<rozvrh>
		<xsl:call-template name="zaSemestr">
			<xsl:with-param name="semestr" select="'ZS'" />
		</xsl:call-template>
		<xsl:call-template name="zaSemestr">
			<xsl:with-param name="semestr" select="'LS'" />
		</xsl:call-template>
	</rozvrh>
</xsl:template>


<xsl:template match="*|/" mode="faze2">
	<xsl:copy>
		<xsl:apply-templates mode="faze2" />
	</xsl:copy>
</xsl:template>

<xsl:template match="radka" mode="faze2">
	<xsl:variable name="radka" select="." />

	<!-- Vypočítat maximum kolizí pro daný den. -->
	<xsl:variable name="maxKolizi">
		<xsl:for-each select="$data/data/casy/hodina">
				<xsl:sort data-type="number" order="descending"
	select="count($radka/predmet[xs:time(zacatek) &lt;= xs:time(current()/zacatek)
						and xs:time(konec) &gt;= xs:time(current()/konec)])" />
			<xsl:if test="position() = 1">
				<xsl:value-of
	select="count($radka/predmet[xs:time(zacatek) &lt;= xs:time(current()/zacatek)
					and xs:time(konec) &gt;= xs:time(current()/konec)])" />

			</xsl:if>
		</xsl:for-each>
	</xsl:variable>

	<faze2den>
		<rowspan><xsl:value-of select="$maxKolizi" /></rowspan>
		<xsl:variable name="predmety" select="predmet" />
		<xsl:apply-templates select="den" mode="faze2" />
		<xsl:for-each select="1 to $maxKolizi">
			<xsl:variable name="i" select="." />
			<faze2radka>
				<!-- Pro každou řádku vybrat ty předměty, které mají $i
					 předcházejících předmětů konfliktních. -->
				<xsl:for-each select=
	"for $p in $predmety
		return
			if (count(
					$predmety[xs:time(zacatek) &lt;= xs:time($p/zacatek)
						and xs:time(konec) &gt;= xs:time($p/zacatek)]) = $i)
				then $p
				else ()">
					<xsl:copy-of select="." />
				</xsl:for-each>
			</faze2radka>
		</xsl:for-each>
	</faze2den>
</xsl:template>
	

<xsl:template match="*|/" mode="faze3">
	<xsl:copy>
		<xsl:apply-templates mode="faze3" />
	</xsl:copy>
</xsl:template>

<xsl:template match="faze2den" mode="faze3">
	<faze3den>
		<xsl:apply-templates mode="faze3" />
	</faze3den>
</xsl:template>


<xsl:template match="faze2radka" mode="faze3">
	<xsl:variable name="predmety" select="predmet" />
	<faze3radka>
		<!-- Vyplnit prázdné hodiny prázdnými předměty. -->
		<xsl:for-each select="$data/data/casy/hodina">
			<xsl:if test="count(
							$predmety[zacatek &lt;= current()/zacatek
								and konec &gt;= current()/konec]
								) = 0">
				<predmet>
					<prazdny>ano</prazdny>
					<xsl:copy-of select="zacatek" />
					<xsl:copy-of select="konec" />
				</predmet>
			</xsl:if>
		</xsl:for-each>

		<!-- Doplnit neprázdné hodiny. -->
		<xsl:apply-templates select="predmet" mode="faze3" />
	</faze3radka>
</xsl:template>






<xsl:variable name="vysledekFaze1">
	<xsl:apply-templates select="/" mode="faze1" />
</xsl:variable>

<!-- Fáze 2: rozdělit dny do jednotlivých řádků podle počtu kolizí. -->
<xsl:variable name="vysledekFaze2">
	<xsl:apply-templates select="$vysledekFaze1" mode="faze2" />
</xsl:variable>

<!-- Fáze 3: pro každý řádek každého dne doplnit prázdné předměty pro prázdné
			 hodiny. -->
<xsl:variable name="vysledekFaze3">
	<xsl:apply-templates select="$vysledekFaze2" mode="faze3" />
</xsl:variable>

<!-- Fáze 4: vytvoření html rozvrhu. -->
<xsl:variable name="vysledekFaze4">
	<xsl:apply-templates select="$vysledekFaze3" mode="faze4" />
</xsl:variable>

<xsl:template match="/">
	<xsl:apply-templates select="$vysledekFaze3" mode="faze3" />
</xsl:template>
</xsl:stylesheet>
