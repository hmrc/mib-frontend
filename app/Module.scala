import com.google.inject.{AbstractModule, Provides, Singleton}
import play.api.Mode.Mode
import play.api.{Configuration, Environment}
import uk.gov.hmrc.play.config.ServicesConfig

class Module() extends AbstractModule {
  override def configure(): Unit = ()

  @Provides
  @Singleton
  def config(configuration: Configuration) = configuration.underlying

  @Provides
  @Singleton
  def serviceConfig(environment: Environment, configuration: Configuration): ServicesConfig = new ServicesConfig {
    def mode: Mode = environment.mode
    def runModeConfiguration: Configuration = configuration
  }
}
